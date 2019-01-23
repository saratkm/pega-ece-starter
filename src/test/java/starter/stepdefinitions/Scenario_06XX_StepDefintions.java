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

	
	String newJson;

	
	//=== Example code ===//
	/***
	@Given("^Shari is currently Receiving FTB scenario six$")
	public void shari_is_currently_Receiving_FTB_scenario_six() {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new PendingException();
		
		String age 					= csvInpData.getEntry(csvDataInpKey, "Cust_DOB");
		String Cust_HdrAddrLine 	= csvInpData.getEntry(csvDataInpKey, "Cust_HdrAddrLine");
		
		System.out.println("Age : "+age+"  CustHdrAddr :"+Cust_HdrAddrLine);
		
		String newAge = age.substring(0,6)+"18";
		String newAdrs = Cust_HdrAddrLine.replaceAll("CARRARA", "canberra");
		
		System.out.println("NewAge : "+newAge+"  CustHdrAddr :"+newAdrs);

		
		jsonDataHndl.selectKey("Customer");
		System.out.println("Before DOB:"+jsonDataHndl.getDataEntry("DOB"));
		jsonDataHndl.putDataEntry("DOB", newAge);
		System.out.println("After DOB:"+jsonDataHndl.getDataEntry("DOB"));
		
		//jsonDataHndl.selectKey("HdrAddressLine");
		System.out.println("Before Addr:"+jsonDataHndl.getDataEntry("HdrAddressLine"));
		jsonDataHndl.putDataEntry("HdrAddressLine", newAdrs);
		System.out.println("After Addr:"+jsonDataHndl.getDataEntry("HdrAddressLine"));
			
		jsonDataHndl.saveAs("D:\\DHS\\7JAN\\body_scenario2_1.json");
	}
	***/
	//========== example code ============


	@Given("^User applies for youth allowance (.*)$")
	public void user_applies_for_youth_allowance(String arg1) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new PendingException();
		configFileReader= new ConfigFileReader();
		baseURI  = configFileReader.getApplicationUrl();		//	"https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
		bodyScn = configFileReader.getBodyScenario("1");  		// 	"D:\\DHS\\7JAN\\body_scenario1.json";
		userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
		passwd   = configFileReader.getPasswd();				//	"rules";
		jsonFP	 = new File(bodyScn);
		newJson = configFileReader.getNewJson();
		
		//CsvParser entries = new CsvParser("C:\\Users\\prateek.bajaj\\Desktop\\6.csv");
		CsvParser entries = new CsvParser("D:\\DHS\\7JAN\\csvInScenario_6.csv");
		
		
		System.out.println(entries.getEntry("Test01ADependantBDependant", "Cust_HdrAddrLine"));
//		String add = entries.getEntry("Test01ADependantBDependant", "Cust_HdrAddrLine");
		String age = entries.getEntry("Test01ADependantBDependant", "Cust_DOB");
		System.out.println("DOB is "+ age);
		
//		List<List<String>> csvTable = new Csv("C:\\Users\\prateek.bajaj\\Desktop\\6.csv").getCsvTable();
//		Csv.displayCsvTable(csvTable);
		
		JsonManipulator a = new JsonManipulator(bodyScn);
		a.selectKey("Customer");
//		a.openArray("Circumstances");
//		a.selectKey("HdrAddressLine");
//		a.openArray("CitizenshipStatus");
//		System.out.println(a.getDataEntry("HdrAddressLine"));
		System.out.println(a.getDataEntry("DOB"));
//		a.putDataEntry("HdrAddressLine", add);
		a.putDataEntry("DOB", age);
		a.saveAs("D:\\DHS\\7JAN\\body_scenario5.1.json");
//		System.out.println(a.getCurrentKey());
//		a.backTrack(2);
//		System.out.println(a.getCurrentKey());
//		a.backTrack(5);
		
	}
	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}


	@When("^Sibling is already receiving youth allowance and is dependant$")
	public void sibling_is_already_receiving_youth_allowance_and_is_dependant() throws IOException {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new PendingException();
		
		
		
		String authCookie = (userId + ":" + passwd);
    	String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
    	String jsonBodyStr = generateStringFromResource(newJson);
    	
		response = RestAssured.given().
				header("Authorization", "Basic "+ authCookieEncoded).	//->working:
				//auth().preemptive().basic(userId, passwd).  			//->working:
				contentType("application/json").
				body(newJson).
				body(jsonBodyStr).
			when().
	        	post(baseURI).
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

	@Then("^Verify the (.*) received by both the siblings$")
	public void verify_the_received_by_both_the_siblings(String arg1) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new PendingException();
	}
	
	
}
