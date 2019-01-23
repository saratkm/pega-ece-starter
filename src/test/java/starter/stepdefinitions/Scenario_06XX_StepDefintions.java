package starter.stepdefinitions;

import java.io.File;
import java.nio.file.Files;

import org.openqa.selenium.WebDriver;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dataProviders.ConfigFileReader;
import dataProviders.JsonManipulator;
import io.restassured.response.Response;
import managers.CsvParser;
import starter.stepdefinitions.Scenario_Common_StepDefintions;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


// ========= import from Prateek =======

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import dataProviders.Csv;
import managers.CsvParser;
import dataProviders.JsonManipulator;

import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import dataProviders.ConfigFileReader;

 //============== end import from pratik ===================

public class Scenario_06XX_StepDefintions {

	WebDriver driver 					=Scenario_Common_StepDefintions.driver;
	ConfigFileReader configFileReader 	=Scenario_Common_StepDefintions.configFileReader;
	String baseURI						=Scenario_Common_StepDefintions.baseURI;
					
	String bodyScn						=Scenario_Common_StepDefintions.bodyScn;
	String userId						=Scenario_Common_StepDefintions.userId;
	String passwd						=Scenario_Common_StepDefintions.passwd;
	File jsonFP							=Scenario_Common_StepDefintions.jsonFP;
	Response response					=Scenario_Common_StepDefintions.response;
	JsonManipulator jsonDataHndl 		=Scenario_Common_StepDefintions.jsonDataHndl;
	CsvParser csvInpData 				=Scenario_Common_StepDefintions.csvInpData;
	CsvParser csvExpData 				=Scenario_Common_StepDefintions.csvExpData;
	String statusTest 					=Scenario_Common_StepDefintions.statusTest;
	String csvDataInpKey 				= Scenario_Common_StepDefintions.csvDataInpKey;
	String csvDataOutKey 				= Scenario_Common_StepDefintions.csvDataOutKey;
	String scenarioNum 					= Scenario_Common_StepDefintions.scenarioNumSaved;

	String srvcUrlScn 					= Scenario_Common_StepDefintions.srvcUrlScn;
	String newJson;	
	String responsejson;
	

	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}

	@When("^User applies for youth allowance where sibling is already receiving youth allowance$")
	public void User_applies_for_youth_allowance_where_sibling_is_already_receiving_youth_allowance() throws IOException {
		//Taken from given
		
//		newJson = configFileReader.getNewJson(scenarioNum);
//		newJson = configFileReader.getFilesPath()+csvDataInpKey+"_Upd01.json";
//		newJson = configFileReader.getNewJson(scenarioNum).replaceAll(".json", "_"+csvDataInpKey+".json");
		newJson = configFileReader.getTmpFilesPath()+ "body_UpdatedScenario6_" + csvDataInpKey + ".json";
		
		CsvParser entries = new CsvParser(configFileReader.getCsvInScenario(scenarioNum));
				
		
		JsonManipulator a = new JsonManipulator(bodyScn);

		String add = entries.getEntry(csvDataInpKey, "Cust_HdrAddrLine");
		String age = entries.getEntry(csvDataInpKey, "Cust_DOB");
		String countrycode = entries.getEntry(csvDataInpKey, "Cust_Circ_Res_BCC");
		String dateofentry = entries.getEntry(csvDataInpKey, "Cust_Circ_Res_DOE");
		String earningamount = entries.getEntry(csvDataInpKey, "P1_Circ_Earn_EarnAmt");
		String ssor_request = entries.getEntry(csvDataInpKey, "Cust_SSOR");
		
		System.out.println("SSOR request is :"+ssor_request);
		
		a.selectKey("Customer");
		a.putDataEntry("DOB", age);
		a.putDataEntry("SSOR", ssor_request);
		a.putDataEntry("HdrAddressLine", add);
		a.openArray("Circumstances",0);
		a.selectKey("Residency");
		a.putDataEntry("BirthCountryCode", countrycode);
		a.putDataEntry("DateofEntry", dateofentry);
		
		a.backTrack(3);
		
		a.openArray("Parents",1);
		a.openArray("Circumstances",0);
		a.openArray("Earnings", 0);
		a.putDataEntry("EarningsAmount", earningamount);
		
		a.saveAs(newJson);
	
//When starts from here
	
		String authCookie = (userId + ":" + passwd);
    	String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
    	String jsonBodyStr = generateStringFromResource(newJson);
    	System.out.println("url hitting is"+srvcUrlScn);
    	
		response = RestAssured.given().
				header("Authorization", "Basic "+ authCookieEncoded).	//->working:
				//auth().preemptive().basic(userId, passwd).  			//->working:
				contentType("application/json").
				body(jsonBodyStr).
				when().
	        	post(srvcUrlScn).
	        then().
	        	statusCode(200).
	        	contentType(ContentType.JSON).
	        	log().all().
	        extract().
	        	response();

	    	System.out.println("Status Code  :"+ response.statusCode());
	    	System.out.println("Content Type :"+ response.contentType());
	    	
	    	
	    	 // Retrieve the body of the Response
	    	 ResponseBody body = response.getBody();
	}
	
	

	@Then("^Verify the youth allowance received by both the siblings$")
	public void verify_the_youth_allowance_received_by_both_the_siblings() {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new PendingException();
		
		 responsejson = configFileReader.getFilesPath();
		
		
		ResponseBody body = response.getBody();
		//Grabs the response body and makes a JSONObject of it
				JSONObject jsonObject = new JSONObject();
					try {
					jsonObject = new JSONObject( body.asString() );
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} 
				//Saves the response as a JSON file
				
//				String nJson =newJson.replaceAll(".json","_Rsp01.json");
				String nJson = configFileReader.getTmpFilesPath()+csvDataInpKey+"_Rsp01.json";
				String filePath = nJson;
				
				JsonManipulator.saveAs(jsonObject, filePath);
				
//				Loads JSON file into JsonManipulator class
				
//				JsonManipulator a = new JsonManipulator(filePath);
		
				//CSV Parsing to get values
				CsvParser response_Entries = new CsvParser(configFileReader.getCsvExpectedScenario(scenarioNum));	
				
				String name = response_Entries.getEntry(csvDataOutKey, "AR_Cust_HdrNameLine");
				String ssor = response_Entries.getEntry(csvDataOutKey, "AR_Cust_SSOR");
				
				//Json response data fetching
				JsonManipulator b = new JsonManipulator(nJson);
				b.selectKey("AssessmentResults");
				b.openArray("Customers", 0);
				b.getDataEntry("HdrNameLine");
				String name_Rsp = b.getDataEntry("HdrNameLine");
				String ssor_Rsp = b.getDataEntry("SSOR");
				b.openArray("PaymentCycles", 0);
				String startDate_Rsp = b.getDataEntry("StartDate");
				b.openArray("Claims", 0);
				String amr_Rsp = b.getDataEntry("AMR");
				String benefitTypeCode_Rsp = b.getDataEntry("BenefitTypeCode");
				String amount_Rsp = b.getDataEntry("Amount");
				b.selectKey("Calculations");
				String isEligible_Rsp = b.getDataEntry("IsEligible");
				b.backTrack(1);
				String dov_Rsp = b.getDataEntry("DOV");
				String reasonCode_Rsp = b.getDataEntry("ReasonCode");
				b.backTrack(1);
				String endDate_Rsp = b.getDataEntry("EndDate");
			
				System.out.println("SSOR response is :"+ssor_Rsp);
				
				Assert.assertTrue(name.equals(name_Rsp));
				Assert.assertTrue(ssor.equals(ssor_Rsp));
		
		}
	
	
	@When("^User relocates while receiving youth allowance where sibling is already receiving youth allowance and is dependant$")
	public void user_relocates_while_receiving_youth_allowance_where_sibling_is_already_receiving_youth_allowance_and_is_dependant() throws IOException {
	    // Write code here that turns the phrase above into concrete actions
		
		
//		newJson = configFileReader.getNewJson(scenarioNum).replaceAll(".json", "_"+csvDataInpKey+".json");
		newJson = configFileReader.getTmpFilesPath()+ "body_UpdatedScenario6_" + csvDataInpKey + ".json";
		CsvParser entries = new CsvParser(configFileReader.getCsvInScenario(scenarioNum));
		JsonManipulator a = new JsonManipulator(bodyScn);
		
		
		String add = entries.getEntry(csvDataInpKey, "Cust_HdrAddrLine");
		String age = entries.getEntry(csvDataInpKey, "Cust_DOB");
		String countrycode = entries.getEntry(csvDataInpKey, "Cust_Circ_Res_BCC");
		String dateofentry = entries.getEntry(csvDataInpKey, "Cust_Circ_Res_DOE");
		String earningamount = entries.getEntry(csvDataInpKey, "P1_Circ_Earn_EarnAmt");
		String ssor_request = entries.getEntry(csvDataInpKey, "Cust_SSOR");
		
		
		a.selectKey("Customer");
		a.putDataEntry("DOB", age);
		a.putDataEntry("SSOR", ssor_request);
		a.putDataEntry("HdrAddressLine", add);
		a.openArray("Circumstances",0);
		a.selectKey("Residency");
		a.putDataEntry("BirthCountryCode", countrycode);
		a.putDataEntry("DateofEntry", dateofentry);
		
		a.backTrack(3);
		
		a.openArray("Parents",1);
		a.openArray("Circumstances",0);
		a.openArray("Earnings", 0);
		a.putDataEntry("EarningsAmount", earningamount);
		
		a.saveAs(newJson);
// POST COMMANDS
		
		String authCookie = (userId + ":" + passwd);
    	String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
    	String jsonBodyStr = generateStringFromResource(newJson);
    	System.out.println("url hitting is"+srvcUrlScn);
    	
		response = RestAssured.given().
				header("Authorization", "Basic "+ authCookieEncoded).	//->working:
				//auth().preemptive().basic(userId, passwd).  			//->working:
				contentType("application/json").
				body(jsonBodyStr).
				when().
	        	post(srvcUrlScn).
	        then().
	        	statusCode(200).
	        	contentType(ContentType.JSON).
	        	log().all().
	        extract().
	        	response();

	    	System.out.println("Status Code  :"+ response.statusCode());
	    	System.out.println("Content Type :"+ response.contentType());
	    	
	    	
	    	 // Retrieve the body of the Response
	    	 ResponseBody body = response.getBody();
		
		
	}


	@Then("^Verify the youth allowance received by user and the sibling$")
	public void verify_the_youth_allowance_received_by_user_and_the_sibling() {
	    // Write code here that turns the phrase above into concrete actions
		
		
		 responsejson = configFileReader.getFilesPath();
			
			
			ResponseBody body = response.getBody();
			//Grabs the response body and makes a JSONObject of it
					JSONObject jsonObject = new JSONObject();
						try {
						jsonObject = new JSONObject( body.asString() );
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					} 
					//Saves the response as a JSON file
					
//					String nJson =newJson.replaceAll(".json", "_Rsp01.json");
//					String nJson = configFileReader.getFilesPath()+csvDataInpKey+"_Rsp01.json";
					String nJson = configFileReader.getTmpFilesPath()+csvDataInpKey+"_Rsp01.json";
					String filePath = nJson;
					
					JsonManipulator.saveAs(jsonObject, filePath);
					//Loads JSON file into JsonManipulator class
//					JsonManipulator a = new JsonManipulator(filePath);
			
					//CSV Parsing to get values
					CsvParser response_Entries = new CsvParser(configFileReader.getCsvExpectedScenario(scenarioNum));	
					
					String name = response_Entries.getEntry(csvDataOutKey, "AR_Cust_HdrNameLine");
					String ssor = response_Entries.getEntry(csvDataOutKey, "AR_Cust_SSOR");
					
					//Json response data fetching
					JsonManipulator b = new JsonManipulator(nJson);
					b.selectKey("AssessmentResults");
					b.openArray("Customers", 0);
					b.getDataEntry("HdrNameLine");
					String name_Rsp = b.getDataEntry("HdrNameLine");
					String ssor_Rsp = b.getDataEntry("SSOR");
					b.openArray("PaymentCycles", 0);
					String startDate_Rsp = b.getDataEntry("StartDate");
					b.openArray("Claims", 0);
					String amr_Rsp = b.getDataEntry("AMR");
					String benefitTypeCode_Rsp = b.getDataEntry("BenefitTypeCode");
					String amount_Rsp = b.getDataEntry("Amount");
					b.selectKey("Calculations");
					String isEligible_Rsp = b.getDataEntry("IsEligible");
					b.backTrack(1);
					String dov_Rsp = b.getDataEntry("DOV");
					String reasonCode_Rsp = b.getDataEntry("ReasonCode");
					b.backTrack(1);
					String endDate_Rsp = b.getDataEntry("EndDate");
				
					System.out.println("SSOR response is :"+ssor_Rsp);
					
					Assert.assertTrue(name.equals(name_Rsp));
					Assert.assertTrue(ssor.equals(ssor_Rsp));
		
		
		
	    
	}
	
	
}
