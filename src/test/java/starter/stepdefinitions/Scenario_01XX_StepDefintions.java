package starter.stepdefinitions;

import java.io.File;

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
import org.junit.Assert;
import org.json.JSONArray;
import org.json.JSONException;


//-------- from TIM ---------------
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import dataProviders.ConfigFileReader;

public class Scenario_01XX_StepDefintions {

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
	String csvDataInpKey 				=Scenario_Common_StepDefintions.csvDataInpKey;
	String csvDataOutKey 				=Scenario_Common_StepDefintions.csvDataOutKey;
	String scenarioNo 					=Scenario_Common_StepDefintions.scenarioNo;
	String configPath 					=Scenario_Common_StepDefintions.configPath;
	//----------- IMPORT FROM CAN START---------
	String payloadFilePath;
	String inputCsvFilePath;
	String expectedOutputCsvFilePath;
	//----------- IMPORT FROM CAN END---------
	
	@When("^User applies for youth allowance$")
	public void amy_applies_for_youth_allowance() {
		payloadFilePath = configFileReader.getNewJson(scenarioNo);
		inputCsvFilePath = configFileReader.getCsvInScenario(scenarioNo);
		expectedOutputCsvFilePath = configFileReader.getCsvExpectedScenario(scenarioNo);
		//baseURI = "https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/CLM_YAL";
       	
		//Gets the data from CSV file
		csvInpData = new CsvParser(inputCsvFilePath, csvDataInpKey);
		System.out.println(inputCsvFilePath);
		//Prepares the JSON payload to be sent to the Pega server
		prepareJSONPayloadTC1();
		System.out.println("Finished modifying JSON payload with test data from CSV file");
		
		//JSON Payload
		jsonFP = new File(payloadFilePath);
    	
    	//Setup for logging into Pega
		String authCookie = (userId + ":" + passwd);
    	String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
		
    	//Posting to Pega and getting Response
    	response = RestAssured.
    		given().header("Authorization", "Basic "+ authCookieEncoded).contentType("application/json").body(jsonFP).
    		when().post(baseURI).
            then().statusCode(200).contentType(ContentType.JSON).log().all().extract().response();
    	System.out.println("Posted JSON payload to PEGA server and got JSON response");
    	
	}
	
	@Then("^Calculate eligibility, entitlement for user$")
	public void calculate_eligibility_entitlement_for_Amy() {
		System.out.println("Status Code  :"+ response.statusCode());
    	System.out.println("Content Type :"+ response.contentType());
    	
		// Retrieve the body of the Response
		ResponseBody body = response.getBody();
		 
		//=== STRING CASTING METHODS ====//
		// By using the ResponseBody.asString() method, we can convert the  body into the string representation.
		System.out.println("Response Body is: " + body.asString());
		 
		//Grabs the response body and makes a JSONObject of it
		JSONObject jsonBodyObject = JsonManipulator.stringToObject(body.asString());
		
		//Creates a JsonManipulator object of the response
		JsonManipulator responseJson = new JsonManipulator(jsonBodyObject);
		
		//Creates a CsvParser object containing the data of expected output csv file
		csvExpData = new CsvParser(expectedOutputCsvFilePath, csvDataOutKey);
		

		System.out.println("Loaded expected Output CSV file");
		responseJson.selectKey("AssessmentResults");
		responseJson.openArray("Customers",0);
		responseJson.openArray("PaymentCycles",0);
		responseJson.openArray("Claims",0);
		responseJson.selectKey("Calculations");
		
		String eligibility = responseJson.getDataEntry("IsEligible");
		System.out.println("Asserting IsEligible. Response = " + eligibility);
		Assert.assertEquals(csvExpData.getEntry("AR_Cust_PC_Claims_Calculations_IsEligible"), eligibility);
		//Assert.assertEquals("0", responseJson.getDataEntry("BasicRate"));
		responseJson.backTrack(3);
		System.out.println("Asserting SSOR");
		Assert.assertEquals(csvExpData.getEntry("AR_Cust_SSOR"), responseJson.getDataEntry("SSOR"));
		responseJson.backTrack();
		System.out.println("Finished asserting comparison between response and expected output");
		responseJson.saveAs( configFileReader.getTmpFilesPath()+"response.json");
		System.out.println("End. Response = " + eligibility);
	}
	
	public void prepareJSONPayloadTC1() {
		JsonManipulator jsonPayload = new JsonManipulator(bodyScn);	
		csvInpData.displayRow();

		jsonPayload.putDataEntry("RequestExecutionDate", csvInpData.getEntry("ReqExecDate"));
		
		jsonPayload.selectKey("Customer");
		jsonPayload.putDataEntry("DOB", csvInpData.getEntry("Cust_DOB"));
		jsonPayload.putDataEntry("SSOR", csvInpData.getEntry("Cust_SSOR"));
		jsonPayload.putDataEntry("HdrAddressLine", csvInpData.getEntry("Cust_HdrAddrLine"));
		jsonPayload.goToRoot();
		
		jsonPayload.selectKey("Customer");
		jsonPayload.selectKey("Address");
		jsonPayload.putDataEntry("AddressLine2", csvInpData.getEntry("Cust_Addr_AddrLine2"));
		jsonPayload.putDataEntry("DOE", csvInpData.getEntry("Cust_Addr_DOE"));
		jsonPayload.putDataEntry("Rental2WeAmnt", csvInpData.getEntry("Cust_Addr_Rental2WeAmnt"));
		jsonPayload.goToRoot();
		
		jsonPayload.selectKey("Customer");
		jsonPayload.selectKey("MaritalStatus");
		jsonPayload.putDataEntry("MaritalStatusCode", csvInpData.getEntry("Cust_MarStat_MarStatCode"));
		jsonPayload.putDataEntry("DOV", csvInpData.getEntry("Cust_MarStat_DOV"));
		jsonPayload.goToRoot();
		
		jsonPayload.selectKey("Customer");
		jsonPayload.openArray("Circumstances",0);
		jsonPayload.selectKey("Residency");
		jsonPayload.putDataEntry("BirthCountryCode", csvInpData.getEntry("Cust_Circ_Res_BCC"));
		jsonPayload.putDataEntry("DateOfResidency", csvInpData.getEntry("Cust_Circ_Res_DOR"));
		jsonPayload.putDataEntry("DateofEntry", csvInpData.getEntry("Cust_Circ_Res_DOE"));
		jsonPayload.openArray("CitizenshipStatus",0);
		jsonPayload.putDataEntry("CountryCitizenCode", csvInpData.getEntry("Cust_Circ_Res_CitStat_CCC"));
		jsonPayload.putDataEntry("DateOfEntry", csvInpData.getEntry("Cust_Circ_Res_CitStat_DOE"));
		jsonPayload.goToRoot();
		
		jsonPayload.selectKey("Customer");
		jsonPayload.openArray("Circumstances",0);
		jsonPayload.selectKey("Residency");
		jsonPayload.selectKey("Visa");
		jsonPayload.putDataEntry("GrantDate", csvInpData.getEntry("Cust_Circ_Visa_GrantDate"));
		jsonPayload.putDataEntry("VisaNumber", csvInpData.getEntry("Cust_Circ_Visa_VisaNo"));
		jsonPayload.putDataEntry("VisaSubclassCode", csvInpData.getEntry("Cust_Circ_Visa_VisaSubclassCode"));
		jsonPayload.goToRoot();
		
		jsonPayload.selectKey("Customer");
		jsonPayload.openArray("Circumstances",0);
		jsonPayload.selectKey("Education");
		jsonPayload.putDataEntry("EducationLevel", csvInpData.getEntry("Cust_Circ_Educ_EducLevel"));
		jsonPayload.putDataEntry("StudentStatusCode", csvInpData.getEntry("Cust_Circ_Educ_StudentStatusCode"));
		jsonPayload.openArray("Courses",0);
		jsonPayload.putDataEntry("CourseStartDate", csvInpData.getEntry("Cust_Circ_Educ_Courses_CourseStartDate"));
		jsonPayload.putDataEntry("CourseEndDate", csvInpData.getEntry("Cust_Circ_Educ_Courses_CourseEndDate"));
		jsonPayload.putDataEntry("DateOfEntry", csvInpData.getEntry("Cust_Circ_Educ_Courses_DOE"));
		jsonPayload.putDataEntry("StudentParticipationStatus", csvInpData.getEntry("Cust_Circ_Educ_Courses_StudentParticipationStatus"));
		jsonPayload.goToRoot();//potential point of error
		
		//No earnings------------------------
		// uses 25,26,27,28
		jsonPayload.selectKey("Customer");
		jsonPayload.openArray("Claims",0);
		jsonPayload.putDataEntry("BenefitTypeCode", csvInpData.getEntry("Cust_Claims_BenefitType"));
		jsonPayload.putDataEntry("DOV", csvInpData.getEntry("Cust_Claims_DOV"));
		jsonPayload.goToRoot();
		
		//Parent 1
		jsonPayload.openArray("Parents",0);
		jsonPayload.openArray("Kids",0);
		jsonPayload.putDataEntry("SSR", csvInpData.getEntry("P1_Kids_SSR"));
		jsonPayload.openArray("Claims",0);
		jsonPayload.putDataEntry("BenefitTypeCode", csvInpData.getEntry("P1_Kids_Claims_BenefitType"));
		jsonPayload.putDataEntry("DOV", csvInpData.getEntry("P1_Kids_Claims_DOV"));
		jsonPayload.goToRoot();
		
		//No earnings for parent 1
		//uses 34,35,36,37
		jsonPayload.openArray("Parents",1);
		jsonPayload.openArray("Kids",0);
		jsonPayload.putDataEntry("SSR", csvInpData.getEntry("P2_Kids_SSR"));
		jsonPayload.goToRoot();
		//no claims for parent 2
		//uses 39,40
		jsonPayload.openArray("Parents",1);
		jsonPayload.openArray("Circumstances",0);
		jsonPayload.openArray("Earnings",0);
		jsonPayload.putDataEntry("IncomeFreqCode", csvInpData.getEntry("P2_Circ_Earn_IncomeFreq"));
		jsonPayload.putDataEntry("DateOfVerification", csvInpData.getEntry("P2_Circ_Earn_DOV"));
		jsonPayload.putDataEntry("EarningsAmount", csvInpData.getEntry("P2_Circ_Earn_EarnAmt"));
		
		jsonPayload.saveAs(payloadFilePath);
	}
	
	public void prepareJSONPayload() {
		JsonManipulator jsonPayload = new JsonManipulator(bodyScn);	
		csvInpData.displayRow();

		jsonPayload.putDataEntry("RequestExecutionDate", csvInpData.getEntry("ReqExecDate"));
		jsonPayload.selectKey("Customer");
		jsonPayload.putDataEntry("DOB", csvInpData.getEntry("Cust_DOB"));
		jsonPayload.putDataEntry("SSOR", csvInpData.getEntry("Cust_SSOR"));
		jsonPayload.putDataEntry("HdrAddressLine", csvInpData.getEntry("Cust_HdrAddrLine"));
		jsonPayload.selectKey("Address");
		jsonPayload.putDataEntry("AddressLine2", csvInpData.getEntry("Cust_Addr_AddrLine2"));
		jsonPayload.putDataEntry("DOE", csvInpData.getEntry("Cust_Addr_DOE"));
		jsonPayload.putDataEntry("Rental2WeAmnt", csvInpData.getEntry("Cust_Addr_Rental2WeAmnt"));
		jsonPayload.backTrack();
		jsonPayload.selectKey("MaritalStatus");
		jsonPayload.putDataEntry("MaritalStatusCode", csvInpData.getEntry("Cust_MarStat_MarStatCode"));
		jsonPayload.putDataEntry("DOV", csvInpData.getEntry("Cust_MarStat_DOV"));
		jsonPayload.backTrack();
		jsonPayload.openArray("Circumstances",0);
		jsonPayload.selectKey("Residency");
		jsonPayload.putDataEntry("BirthCountryCode", csvInpData.getEntry("Cust_Circ_Res_BCC"));
		jsonPayload.putDataEntry("DateOfResidency", csvInpData.getEntry("Cust_Circ_Res_DOR"));
		jsonPayload.putDataEntry("DateofEntry", csvInpData.getEntry("Cust_Circ_Res_DOE"));
		jsonPayload.openArray("CitizenshipStatus",0);
		jsonPayload.putDataEntry("CountryCitizenCode", csvInpData.getEntry("Cust_Circ_Res_CitStat_CCC"));
		jsonPayload.putDataEntry("DateOfEntry", csvInpData.getEntry("Cust_Circ_Res_CitStat_DOE"));
		jsonPayload.backTrack();
		jsonPayload.selectKey("Visa");
		jsonPayload.putDataEntry("GrantDate", csvInpData.getEntry("Cust_Circ_Visa_GrantDate"));
		jsonPayload.putDataEntry("VisaNumber", csvInpData.getEntry("Cust_Circ_Visa_VisaNo"));
		jsonPayload.putDataEntry("VisaSubclassCode", csvInpData.getEntry("Cust_Circ_Visa_VisaSubclassCode"));
		jsonPayload.backTrack(2);
		jsonPayload.selectKey("Education");
		jsonPayload.putDataEntry("EducationLevel", csvInpData.getEntry("Cust_Circ_Educ_EducLevel"));
		jsonPayload.putDataEntry("StudentStatusCode", csvInpData.getEntry("Cust_Circ_Educ_StudentStatusCode"));
		jsonPayload.openArray("Courses",0);
		jsonPayload.putDataEntry("CourseStartDate", csvInpData.getEntry("Cust_Circ_Educ_Courses_CourseStartDate"));
		jsonPayload.putDataEntry("CourseEndDate", csvInpData.getEntry("Cust_Circ_Educ_Courses_CourseEndDate"));
		jsonPayload.putDataEntry("DateOfEntry", csvInpData.getEntry("Cust_Circ_Educ_Courses_DOE"));
		jsonPayload.putDataEntry("StudentParticipationStatus", csvInpData.getEntry("Cust_Circ_Educ_Courses_StudentParticipationStatus"));
		jsonPayload.backTrack(3);
		//jsonPayload.backTrack(2);
		//No earnings
		// uses 25,26,27,28
		jsonPayload.openArray("Claims",0);
		//jsonPayload.putDataEntry("BenefitTypeCode", csvInpData.getEntry("Cust_Claims_BenefitTypeCode"));
		//jsonPayload.putDataEntry("DOV", csvInpData.getEntry("Cust_Claims_DOV"));
		jsonPayload.backTrack(2);
		//Parent 1
		jsonPayload.openArray("Parents",0);
		jsonPayload.openArray("Kids",0);
		//jsonPayload.putDataEntry("SSR", csvInpData.getEntry("P1_Kids_SSR"));
		jsonPayload.openArray("Claims",0);
		//jsonPayload.putDataEntry("BenefitTypeCode", csvInpData.getEntry("P1_Kids_Claims_BenefitTypeCode"));
		//jsonPayload.putDataEntry("DOV", csvInpData.getEntry("P1_Kids_Claims_DOV"));
		//No earnings for parent 1
		// jsonPayload.backTrack(2);
		// uses 34,35,36,37
		jsonPayload.backTrack(3);
		jsonPayload.openArray("Parents",1);
		jsonPayload.openArray("Kids",0);
		//jsonPayload.putDataEntry("SSR", csvInpData.getEntry("P2_Kids_SSR"));
		//no claims for parent 2
		// uses 39,40
		jsonPayload.backTrack();
		jsonPayload.openArray("Circumstances",0);
		jsonPayload.openArray("Earnings",0);
		//jsonPayload.putDataEntry("IncomeFreqCode", csvInpData.getEntry("P2_Circ_Earn_IncomeFreqCode"));
		//jsonPayload.putDataEntry("DateOfVerification", csvInpData.getEntry("P2_Circ_Earn_DOV"));
		//jsonPayload.putDataEntry("EarningsAmount", csvInpData.getEntry("P2_Circ_Earn_EarnAmt"));
		
		jsonPayload.saveAs(payloadFilePath);
	}
}
