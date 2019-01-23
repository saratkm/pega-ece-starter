package starter.stepdefinitions;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dataProviders.JsonManipulator;

public class Main {
	public static void main(String[] args) throws JSONException {
//		List<List<String>> csvTable = new Csv("D:\\DHS\\7JAN\\TestScenario- 3- JV.csv").getCsvTable();
//		Csv.displayCsvTable(csvTable);
		
		JsonManipulator a = new JsonManipulator("D:\\DHS\\7JAN\\body_scenario3.json");
		a.selectKey("Customer");
		a.openArray("Circumstances", 0);
		a.putDataEntry("test", "test");
		a.removeDataEntry("IncomeBank");
		a.removeDataEntryForArray("Earnings", 1);
		
//		JSONObject newEarning = new JSONObject();
//        newEarning.put("id", "10");
//        newEarning.put("AMR", "1792");
////        JSONArray earnings = a.openArray("Earnings");
////        earnings.put(newEarning);
//        a.putDataEntryForArray("Earnings", newEarning);
//        a.putDataEntry("Earnings", newEarning);        
        
//		a.openArray("Earnings", 0);
//		a.openArray("CitizenshipStatus");
//		System.out.println(a.getDataEntry("CountryCitizenCode"));
//		a.putDataEntry("CountryCitizenCode", "IN");
		a.saveAs("D:\\DHS\\7JAN\\body_scenario3_1.json");
		System.out.println(a.getCurrentKey());
		a.backTrack(2);
		System.out.println(a.getCurrentKey());

	}
}
