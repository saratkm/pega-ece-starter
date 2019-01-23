package dataProviders;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import managers.CsvParser;

//openArray is for "[]" Brackets
//selectKey is for "{}" Brackets
public class JsonManipulator {
	
	JSONObject entryPointer = new JSONObject();
	JSONObject masterJSONObject = new JSONObject();
	Stack<JSONObject> travelHistory = new Stack<JSONObject>();
	Stack<String> travelHistoryIdentifier = new Stack<String>();
	
	public JsonManipulator(String filePath) {
		masterJSONObject = convertJsonFileToObject(filePath); //Creates JSON Object from file
		entryPointer = masterJSONObject;
		travelHistory.push(entryPointer);
		travelHistoryIdentifier.push("Top of JSON file");
	}
	
	public JsonManipulator(JSONObject object) {
		masterJSONObject = object; 
		entryPointer = masterJSONObject;
		travelHistory.push(entryPointer);
		travelHistoryIdentifier.push("Top of JSON file");
	}
	
	public void backTrack() {
		if (travelHistory.size() > 1 && travelHistoryIdentifier.size() > 1) {
			travelHistory.pop();
			travelHistoryIdentifier.pop();	
		}
		entryPointer = travelHistory.peek();
	}
	
	public void backTrack(int number) {
		for (int i = 0; i < number; i++) {
			if (travelHistory.size() > 1 && travelHistoryIdentifier.size() > 1) {
				travelHistory.pop();
				travelHistoryIdentifier.pop();	
			}
		}
		entryPointer = travelHistory.peek();
	}
	
	public String getCurrentKey() {
		return travelHistoryIdentifier.peek();
	}
	
	public JSONObject getCurrentObject() {
		return entryPointer;
	}
	
	public JSONObject getMasterObject() {
		return masterJSONObject;
	}
	
	public void putDataEntry(String key, String Data) {
		try {
			entryPointer.put(key, Data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void putDataEntry(String key, JSONObject Data) {
		try {
			entryPointer.put(key, Data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void putDataEntry(String key, JSONArray Data) {
		try {
			entryPointer.put(key, Data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	static public void putDataEntry(String key, String Data, JSONObject object) {
		try {
			object.put(key, Data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	static public String getDataEntry(JSONObject object, String key) {
		try {
			return object.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getDataEntry(String key) {
		try {
			return entryPointer.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public JSONObject selectKey(String dataEntry) {
		try {
			entryPointer = entryPointer.getJSONObject(dataEntry);
			travelHistory.push(entryPointer);
			travelHistoryIdentifier.push(dataEntry);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entryPointer;
	}
	
	static public JSONObject selectKey(JSONObject object, String dataEntry) {
		try {
			return object.getJSONObject(dataEntry);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONObject openArray(String dataEntry, int index) {
		try {
			entryPointer = entryPointer.getJSONArray(dataEntry).getJSONObject(index); //Creates a JSON Array for editing
			travelHistory.push(entryPointer);
			travelHistoryIdentifier.push(dataEntry);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entryPointer;
	}
	
	static public JSONObject openArray(JSONObject object, String dataEntry, int index) {
		try {
			return object.getJSONArray(dataEntry).getJSONObject(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONObject convertJsonFileToObject(String filePath) {
		String jsonBodyStr = "";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonBodyStr = generateStringFromResource(filePath);
			jsonObject = new JSONObject( jsonBodyStr ); 	
		} catch (IOException e) {
			e.printStackTrace();
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  jsonObject;	
	}

	static public void saveAs(JSONObject object, String newFilePath) {//newFilePath needs to include name and extension of the file
		File f = new File(newFilePath);
		if (f.exists()) {
			System.out.println("File already exists, overwriting old file");	
		}
		try (FileWriter file = new FileWriter(f, false)) {
			file.write(object.toString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void saveAs(String newFilePath) {//newFilePath needs to include name and extension of the file
		File f = new File(newFilePath);
		if (f.exists()) {
			System.out.println("File already exists, overwriting old file");	
		}
		try (FileWriter file = new FileWriter(f, false)) {
			file.write(masterJSONObject.toString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	static public JSONObject stringToObject(String jsonString) {
		try {
			return new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void goToRoot() {
		backTrack(travelHistory.size());
	}
	
	//JV's Functions
	public void putDataEntryForArray(String key, JSONObject newObject) {
		try {
			JSONArray newArray = entryPointer.getJSONArray(key);
			newArray.put(newObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void removeDataEntry(String key) {
		try {
			entryPointer.remove(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeDataEntryForArray(String key, int index) {
		try {
			JSONArray arr = entryPointer.getJSONArray(key);
			arr.remove(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONArray getJSONArray(String dataEntry) {
		try {
			return entryPointer.getJSONArray(dataEntry); //Creates a JSON Array for editing
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	public String getDataEntryByPath(String entryPath) throws JSONException {
		String [] strArray;
		goToRoot();
		String[] strPath = entryPath.split("\\.");
		for(int i = 0; i < strPath.length-1; i++) {
			if(strPath[i].indexOf(':') == -1) {
				if(entryPointer.has(strPath[i]))
					selectKey(strPath[i]);
				else
					return null;
			}
			else {
				strArray = strPath[i].split(":");
				if(entryPointer.has(strArray[0]) && entryPointer.getJSONArray(strArray[0]).length() > 0) {
					openArray(strArray[0], Integer.parseInt(strArray[1]));
				}
				else
					return null;
			}
		}
		return getDataEntry(strPath[strPath.length-1]);
	}
	
	public void putDataEntryByPath(String entryPath, String Data) throws JSONException {
		if(Data.isEmpty()) return;
		String [] strArray;
		goToRoot();
		String[] strPath = entryPath.split("\\.");
		for(int i = 0; i < strPath.length-1; i++) {
			if(strPath[i].indexOf(':') == -1) {
				if(entryPointer.has(strPath[i]))
					selectKey(strPath[i]);
				else {
					JSONObject newObjEntry = new JSONObject();
					putDataEntry(strPath[i], newObjEntry);
					selectKey(strPath[i]);
				}
			}
			else {
				strArray = strPath[i].split(":");
				if(entryPointer.has(strArray[0]) && entryPointer.getJSONArray(strArray[0]).length() > Integer.parseInt(strArray[1])) {
					openArray(strArray[0], Integer.parseInt(strArray[1]));
				}
				else{
					JSONObject newObjEntry = new JSONObject();
					putDataEntryForArray(strArray[0], newObjEntry);
					openArray(strArray[0], Integer.parseInt(strArray[1]));
				}
			}
		}
		try {
			entryPointer.put(strPath[strPath.length-1], Data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getJSONObjectByPath(String entryPath) throws JSONException {
		String [] strArray;
		goToRoot();
		String[] strPath = entryPath.split("\\.");
		for(int i = 0; i < strPath.length-1; i++) {
			if(strPath[i].indexOf(':') == -1) {
				if(entryPointer.has(strPath[i]))
					selectKey(strPath[i]);
				else
					return null;
			}
			else {
				strArray = strPath[i].split(":");
				if(entryPointer.has(strArray[0]) && entryPointer.getJSONArray(strArray[0]).length() > 0) {
					openArray(strArray[0], Integer.parseInt(strArray[1]));
				}
				else
					return null;
			}
		}
		if(strPath[strPath.length-1].indexOf(':') == -1)
			return selectKey(strPath[strPath.length-1]);
		else {
			strArray = strPath[strPath.length-1].split(":");
			return openArray(strArray[0], Integer.parseInt(strArray[1]));
		}
			
	}
	
	public JSONArray getJSONArrayByPath(String entryPath) throws JSONException {
		String [] strArray;
		goToRoot();
		String[] strPath = entryPath.split("\\.");
		for(int i = 0; i < strPath.length-1; i++) {
			if(strPath[i].indexOf(':') == -1) {
				if(entryPointer.has(strPath[i]))
					selectKey(strPath[i]);
				else
					return null;
			}
			else {
				strArray = strPath[i].split(":");
				if(entryPointer.has(strArray[0]) && entryPointer.getJSONArray(strArray[0]).length() > 0) {
					openArray(strArray[0], Integer.parseInt(strArray[1]));
				}
				else
					return null;
			}
		}
		strArray = strPath[strPath.length-1].split(":");
		return getJSONArray(strArray[0]);
	}
	
	public void updateRequestBody(CsvParser csvInpData, String csvDataInpKey, HashMap<String,String> elementsMap) throws JSONException {
		goToRoot();
		csvInpData.loadRow(csvDataInpKey);
		
		Map<String,String> hmRowEntry = csvInpData.getRow(csvDataInpKey);		
		Map<String, String> lhmRowEntry = new TreeMap<String,String>(hmRowEntry);
		
		Iterator it = lhmRowEntry.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry csvInpElement = (Map.Entry)it.next();
	        if(elementsMap.get(csvInpElement.getKey()) != null) {
	        	putDataEntryByPath(elementsMap.get(csvInpElement.getKey()), csvInpElement.getValue().toString());	        	
	        }
	    }
	}
}
