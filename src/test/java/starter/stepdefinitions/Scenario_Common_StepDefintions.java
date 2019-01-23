package starter.stepdefinitions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.lang.Object;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import junit.framework.Assert;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import dataProviders.ConfigFileReader;
import dataProviders.JsonManipulator;

import managers.CsvParser;



public class Scenario_Common_StepDefintions {

	static WebDriver driver;
	static ConfigFileReader configFileReader;
	static String baseURI, srvcUrlScn, bodyScn, csvInScn, csvExpectedScn, userId, passwd;
	static File jsonFP = null;
	static Response response = null;
	static JsonManipulator jsonDataHndl = null;
	static CsvParser csvInpData = null;
	static CsvParser csvExpData = null;
	static String statusTest = null;
	static String csvDataInpKey =null, csvDataOutKey = null;
	static String configPath = null;
	static String scenarioNo = null;
	static String scenarioNumSaved = null;





	@Given("^User initiatiates scenario \"([^\"]*)\"$")
	public void user_initiatiates_scenario(String scenarioNum) throws JSONException, IOException {

		scenarioNumSaved = scenarioNum;
		configFileReader= new ConfigFileReader();
		baseURI  = configFileReader.getApplicationUrl();		//	"https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";

		srvcUrlScn=configFileReader.getServiceUrl(scenarioNum); // two parts..

		bodyScn=configFileReader.getBodyScenario(scenarioNum);
		csvInScn=configFileReader.getCsvInScenario(scenarioNum);
		csvExpectedScn=configFileReader.getCsvExpectedScenario(scenarioNum);
		scenarioNo = scenarioNum;
		configPath = System.getProperty("user.dir") + "/configs/CSV_JSON/";

		System.out.println("Got CSV & Json files: "+bodyScn+" | "+csvInScn+" | "+csvExpectedScn);

		if (bodyScn!=null && csvInScn!=null && csvExpectedScn!=null) {
			System.out.println("Got CSV & Json files: "+bodyScn+" | "+csvInScn+" | "+csvExpectedScn);
		} else {
			throw new RuntimeException("CSV & Json files missing.Exiting with error :"+bodyScn+"|"+csvInScn+"|"+csvExpectedScn);
		}		

		userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
		passwd   = configFileReader.getPasswd();				//	"rules";
		jsonFP	 = new File(bodyScn);

		if (!JsonObjHndle(bodyScn)) {
			throw new RuntimeException("cannot acquire Json obj handler :"+bodyScn);
		}

		csvInpData = new CsvParser(csvInScn);
		csvExpData = new CsvParser(csvExpectedScn);


	}



	@Given("^Payload data input (.*) expected (.*) status (.*)$")
	public void payload_data_input_expected_status(String csvInKey, String csvExpectedKey, String testStatus) {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();

		//Display new values received per run:
		System.out.println( "TestCriteria ----- VALUES start--------"); 
		System.out.println( "csvInKey      : " + csvInKey );  
		System.out.println( "csvExpectedKey: " + csvExpectedKey ); 
		System.out.println( "testStatus    : " + testStatus +"\n"); 
		System.out.println( "TestCriteria ----- VALUES end-----------"); 

		csvDataInpKey = csvInKey;
		csvDataOutKey = csvExpectedKey;
		statusTest = testStatus;

	}

	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}

	////Use Json File as payload instead of converting to String as above case 
	//public void RestTest() throws Exception {
	//    File file = new File("/Users/bmishra/Code_Center/stash/experiments/src/main/resources/Search.json");
	//    String content = null;
	//
	//    RestAssured.given().body(file).with().contentType("application/json").then().expect().
	//            statusCode(200).
	//            body(Matchers.equalTo("true")).when().post("http://devsearch");
	//}
	////

	public JSONObject convertJsonFileToObject(String jsonPath) throws JSONException, IOException {
		String jsonBodyStr = generateStringFromResource(jsonPath);
		JSONObject test = new JSONObject( jsonBodyStr ); 
		return  test;
	}

	public void createJsonFile(JSONObject newJson, String filepath) {
		String filename, newFilename;
		File f = new File(filepath);
		filename = filepath.substring(filepath.lastIndexOf("\\")+1);
		int version = 1;
		while (f.exists())
		{
			newFilename = filename.substring(0, filename.length()-5) + "_" + version + ".json";
			f = new File(configFileReader.getFilesPath() + newFilename);
			version++;
		}

		try (FileWriter file = new FileWriter(f)) {

			file.write(newJson.toString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

		bodyScn = f.getAbsolutePath();  		
		jsonFP	 = new File(bodyScn);
	}

	public boolean JsonObjHndle(String bodyScn) {

		jsonDataHndl  = new JsonManipulator(bodyScn);
		if (jsonDataHndl==null) {
			System.out.println("Json object handler create erroro for "+bodyScn);
			throw new RuntimeException("Json object handler create erroro for "+bodyScn);
		}
		return true;
	}



	/*////////////

	//JsonManipulator a = new JsonManipulator("C:\\Users\\can.nguyen\\Desktop\\testa.json");
	jsonDataHndl.selectKey("Customer");
	jsonDataHndl.openArray("Circumstances");
	jsonDataHndl.selectKey("Residency");
	jsonDataHndl.openArray("CitizenshipStatus");
	System.out.println(jsonDataHndl.getDataEntry("CountryCitizenCode"));
	jsonDataHndl.putDataEntry("CountryCitizenCode", "IN");
	jsonDataHndl.saveAs("C:\\Users\\can.nguyen\\Desktop\\test2.json");
//	System.out.println(jsonDataHndl.getCurrentKey());
	jsonDataHndl.backTrack(2);
	System.out.println(jsonDataHndl.getCurrentKey());
//	jsonDataHndl.backTrack(5);

//	System.out.println(jsonDataHndl.getCurrentKey());
//	jsonDataHndl.backTrack();
//	System.out.println(jsonDataHndl.getCurrentKey());

//	JSONObject o = a.getCurrentObject();
//	o = Json.selectKey(o,"Customer");
//	System.out.println(Json.getDataEntry(o, "DOB"));
	//jsonDataHndl.saveAs("C:\\Users\\can.nguyen\\Desktop\\test2.json");


	JsonManipulator a = new JsonManipulator("D:\\DHS\\7JAN\\body_scenario1.json");
	a.selectKey("Customer");
	System.out.println(a.getDataEntry("DOB"));
	a.putDataEntry("DOB", age);
	a.saveAs("D:\\DHS\\7JAN\\body_scenario5.1.json");


	 */////


}
