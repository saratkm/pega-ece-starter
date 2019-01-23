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

import dataProviders.Csv;

/*
import dataConversion.*;
*/
//------- END IMPORT FROM TIM -------------

public class Scenario_02XX_StepDefintions {

	WebDriver driver 					=Scenario_Common_StepDefintions.driver;
	ConfigFileReader configFileReader 	=Scenario_Common_StepDefintions.configFileReader;
	String baseURI						=Scenario_Common_StepDefintions.srvcUrlScn;
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

	String bodyScn1,authCookieEncoded,jsonBodyStr;

	//----------- IMPORT FROM TIM START---------
	//WebDriver driver;
	//ConfigFileReader configFileReader;
	//String baseURI, bodyScn1, userId, passwd,authCookieEncoded,jsonBodyStr;
	//File jsonFP=null;
	//Response response = null;
	//----------- IMPORT FROM TIM END---------
	
	
	//=== Example code ===//
	
	@Given("^Shari is (.*)$")
	public void shari_is(String arg1) {
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
		
		List<List<String>> csvTable = new Csv("C:\\Users\\timothy.sultana\\Documents\\Project - Current\\JsonAndCsvFiles\\TestScenario2-Body.csv").getCsvTable();
		Csv.displayCsvTable(csvTable);  // testing demonstration 
		
		//CsvParser csv = new CsvParser("C:\\Users\\can.nguyen\\Desktop\\Expected Output Scenario 1.csv","amyApplies");
		//csv.displayRow();
		//csv.displayTable();
		
		JsonManipulator jsonKey = new JsonManipulator("C:\\Users\\timothy.sultana\\Documents\\Project - Current\\JsonAndCsvFiles\\JsonPayload.json");
		
//		System.out.println(jsonKey.getCurrentKey());
		
		jsonKey.putDataEntry("RequestExecutionDate", csvTable.get(1).get(2));
		jsonKey.selectKey("Customer");
		jsonKey.putDataEntry("DOB", csvTable.get(1).get(3));
		jsonKey.putDataEntry("SSOR", csvTable.get(1).get(4));
		jsonKey.putDataEntry("HdrAddressLine", csvTable.get(1).get(5));
		jsonKey.selectKey("Address");
		jsonKey.putDataEntry("AddressLine2", csvTable.get(1).get(6));
		jsonKey.putDataEntry("DOE", csvTable.get(1).get(7));
		jsonKey.putDataEntry("Rental2WeAmnt", csvTable.get(1).get(8));
		jsonKey.backTrack();
		jsonKey.selectKey("MaritalStatus");
		jsonKey.putDataEntry("MaritalStatusCode", csvTable.get(1).get(9));
		jsonKey.putDataEntry("DOV", csvTable.get(1).get(10));
		jsonKey.backTrack();
		jsonKey.openArray("Circumstances",0);
		jsonKey.selectKey("Residency");
		jsonKey.putDataEntry("BirthCountryCode", csvTable.get(1).get(11));
		jsonKey.putDataEntry("DateOfResidency", csvTable.get(1).get(12));
		jsonKey.putDataEntry("DateofEntry", csvTable.get(1).get(13));
		jsonKey.openArray("CitizenshipStatus",0);
		jsonKey.putDataEntry("CountryCitizenCode", csvTable.get(1).get(14));
		jsonKey.putDataEntry("DateOfEntry", csvTable.get(1).get(15));
		jsonKey.backTrack();
		jsonKey.selectKey("Visa");
		jsonKey.putDataEntry("GrantDate", csvTable.get(1).get(16));
		jsonKey.putDataEntry("VisaNumber", csvTable.get(1).get(17));
		jsonKey.putDataEntry("VisaSubclassCode", csvTable.get(1).get(18));
		jsonKey.backTrack(2);
		jsonKey.selectKey("Education");
		jsonKey.putDataEntry("EducationLevel", csvTable.get(1).get(19));
		jsonKey.putDataEntry("StudentStatusCode", csvTable.get(1).get(20));
		jsonKey.openArray("Courses",0);
		jsonKey.putDataEntry("CourseStartDate", csvTable.get(1).get(21));
		jsonKey.putDataEntry("CourseEndDate", csvTable.get(1).get(22));
		jsonKey.putDataEntry("DateOfEntry", csvTable.get(1).get(23));
		jsonKey.putDataEntry("StudentParticipationStatus", csvTable.get(1).get(24));
		jsonKey.backTrack(3);
		//jsonKey.backTrack(2);
		//No earnings
		// uses 25,26,27,28
		jsonKey.openArray("Claims",0);
		jsonKey.putDataEntry("BenefitTypeCode", csvTable.get(1).get(29));
		jsonKey.putDataEntry("DOV", csvTable.get(1).get(30));
		jsonKey.backTrack(2);
		//Parent 1
		jsonKey.openArray("Parents",0);
		jsonKey.openArray("Kids",0);
		jsonKey.putDataEntry("SSR", csvTable.get(1).get(31));
		jsonKey.openArray("Claims",0);
		jsonKey.putDataEntry("BenefitTypeCode", csvTable.get(1).get(32));
		jsonKey.putDataEntry("DOV", csvTable.get(1).get(33));
		//No earnings for parent 1
		// jsonKey.backTrack(2);
		// uses 34,35,36,37
		jsonKey.backTrack(3);
		jsonKey.openArray("Parents",1);
		jsonKey.openArray("Kids",0);
		jsonKey.putDataEntry("SSR", csvTable.get(1).get(38));
		//no claims for parent 2
		// uses 39,40
		jsonKey.backTrack();
		jsonKey.openArray("Circumstances",0);
		jsonKey.openArray("Earnings",0);
		jsonKey.putDataEntry("IncomeFreqCode", csvTable.get(1).get(41));
		jsonKey.putDataEntry("DateOfVerification", csvTable.get(1).get(42));
		jsonKey.putDataEntry("EarningsAmount", csvTable.get(1).get(43));
		//Scenario 2 additions
		// unfinished
//		jsonKey.putDataEntry("IncomeFreqCode", csvTable.get(1).get(44));
//		jsonKey.putDataEntry("DateOfVerification", csvTable.get(1).get(45));
//		jsonKey.putDataEntry("EarningsAmount", csvTable.get(1).get(46));
//		
//		jsonKey.putDataEntry("IncomeFreqCode", csvTable.get(1).get(47));
//		jsonKey.putDataEntry("DateOfVerification", csvTable.get(1).get(48));
//		jsonKey.putDataEntry("EarningsAmount", csvTable.get(1).get(49));
		
		jsonKey.saveAs("C:\\Users\\timothy.sultana\\Documents\\Project - Current\\JsonAndCsvFiles\\JsonResponse.json");
	
	}
	
	@When("^Amy applies for youth allowance with correct values$")
	public void amy_applies_for_youth_allowance_with_correct_values() throws IOException {
		
		
		configFileReader= new ConfigFileReader();
		
    	baseURI  = configFileReader.getApplicationUrl();		//	"https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
    	//bodyScn1 = "D:\\DHS\\7JAN\\body_scenario2.json";
    	bodyScn1 = "C:\\Users\\timothy.sultana\\Documents\\Project - Current\\JsonAndCsvFiles\\JsonResponse.json";        //"D:\\DHS\\7JAN\\body_scenario1.json";
    	userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
    	passwd   = configFileReader.getPasswd();				//	"rules";
    	jsonFP	 = new File(bodyScn1);
    	String authCookie = (userId + ":" + passwd);
    	authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
    	jsonBodyStr = generateStringFromResource(bodyScn1);
	}
	
	@Then("^Confirm FTB is (.*)$")
	public void confirm_FTB_is(String arg1) {
		response = RestAssured.given().
				header("Authorization", "Basic "+ authCookieEncoded).	//->working:
				contentType("application/json").
				body(jsonFP).
			when().
	        	post(baseURI).
	        then().
	        	statusCode(200).
	        	contentType(ContentType.JSON).
	        	log().all().
	        extract().
	        	response();

//    	System.out.println("Status Code  :"+ response.statusCode()); //demonstration
//    	System.out.println("Content Type :"+ response.contentType());
    	
    	
    	
    	 // Retrieve the body of the Response
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
 		 String filePath = "C:\\Users\\timothy.sultana\\Documents\\Project - Current\\JsonAndCsvFiles\\JsonResponse.json";
 		 JsonManipulator.saveAs(jsonObject, filePath);
 		 //Loads JSON file into JsonManipulator class
 		 JsonManipulator answer = new JsonManipulator(filePath);
 		
    	 
 		 List<List<String>> csvTable = new Csv("C:\\Users\\timothy.sultana\\Documents\\Project - Current\\JsonAndCsvFiles\\Response.csv").getCsvTable();
 		 
    	 
    	 //=== STRING CASTING METHODS ====//
    	 // By using the ResponseBody.asString() method, we can convert the  body into the string representation.
    	 System.out.println("Response Body is: " + body.asString());
    	 //Assert.assertEquals(body.asString().contains("\"StartDate\":\"2018-02-16\""), true);
    	 int answerIndex = body.asString().indexOf("\"Calculations\":{\"IsEligible\":") + 29;
    	 //String answer = body.asString().substring(answerIndex, answerIndex+5);

    	 answer.selectKey("AssessmentResults");
    	 answer.openArray("Customers",0);
    	 //Assert.assertEquals(csvTable.get(1).get(2),answer.getDataEntry("HdrNameLine"));
    	 Assert.assertEquals(csvTable.get(1).get(5),answer.getDataEntry("SSOR"));
    	 answer.openArray("PaymentCycles",0);
    	 Assert.assertEquals(csvTable.get(1).get(6),answer.getDataEntry("StartDate"));
    	 answer.openArray("Claims",0);
    	 Assert.assertEquals(csvTable.get(1).get(7),answer.getDataEntry("AMR"));
    	 Assert.assertEquals(csvTable.get(1).get(8),answer.getDataEntry("BenefitTypeCode"));
    	 Assert.assertEquals(csvTable.get(1).get(9),answer.getDataEntry("Amount").toString().substring(0, 11));
    	 answer.selectKey("Calculations");
    	 Assert.assertEquals(csvTable.get(1).get(10),answer.getDataEntry("ParentalIncomeDeduction").toString().substring(0, 11));
    	 Assert.assertEquals(csvTable.get(1).get(11),answer.getDataEntry("IncomeBankOpeningBalance"));
    	 Assert.assertEquals(csvTable.get(1).get(12),answer.getDataEntry("ParentalIncome"));
    	 Assert.assertEquals(csvTable.get(1).get(13),answer.getDataEntry("Amount").toString().substring(0, 11));
    	 Assert.assertEquals(csvTable.get(1).get(14),answer.getDataEntry("BasicRate"));
    	 Assert.assertEquals(csvTable.get(1).get(15),answer.getDataEntry("IncomeBankCredit"));
    	 Assert.assertEquals(csvTable.get(1).get(16),answer.getDataEntry("IsFulltimeStudent"));
    	 Assert.assertEquals(csvTable.get(1).get(17),answer.getDataEntry("PersonalIncomeDeductionRateBand1"));
    	 Assert.assertEquals(csvTable.get(1).get(18),answer.getDataEntry("PersonalIncomeDeductionRateBand2"));
    	 Assert.assertEquals(csvTable.get(1).get(19),answer.getDataEntry("IsEligible"));
    	 Assert.assertEquals(csvTable.get(1).get(20),answer.getDataEntry("PersonalIncomeDeduction"));
    	 Assert.assertEquals(csvTable.get(1).get(21),answer.getDataEntry("PersonalIncome"));
    	 Assert.assertEquals(csvTable.get(1).get(22),answer.getDataEntry("IncomeBankClosingBalance"));
    	 Assert.assertEquals(csvTable.get(1).get(23),answer.getDataEntry("PersonalIncomeDeductionBand1"));
    	 Assert.assertEquals(csvTable.get(1).get(24),answer.getDataEntry("IsAustralianResident"));
    	 Assert.assertEquals(csvTable.get(1).get(25),answer.getDataEntry("IsParentalIncomeTestRequired"));
    	 Assert.assertEquals(csvTable.get(1).get(26),answer.getDataEntry("PersonalIncomeDeductionBand2"));
    	 Assert.assertEquals(csvTable.get(1).get(27),answer.getDataEntry("ParentalIncomeThreshold"));
    	 Assert.assertEquals(csvTable.get(1).get(28),answer.getDataEntry("Age"));
    	 answer.backTrack();
    	 Assert.assertEquals(csvTable.get(1).get(29),answer.getDataEntry("DOV"));
    	 Assert.assertEquals(csvTable.get(1).get(30),answer.getDataEntry("ReasonCode"));
    	 answer.backTrack();
    	 Assert.assertEquals(csvTable.get(1).get(31),answer.getDataEntry("EndDate"));
    	 answer.backTrack();
    	 answer.openArray("Customers",1);
    	 answer.openArray("PaymentCycles",0);
    	 answer.openArray("Claims",0);
    	 answer.selectKey("Calculations");
    	 Assert.assertEquals(csvTable.get(1).get(32),answer.getDataEntry("IsEligible"));

    	 
    	 
    	 //=== JSON CASTING METHODS ==//
    	 // First get the JsonPath object instance from the Response interface
    	 //JsonPath jsonPathEvaluator = response.jsonPath();
    	 //ArrayList SSOR = jsonPathEvaluator.get("AssessmentResults.Customers[1].SSOR");
    	 //String ssrVal0 = SSOR.get(0).toString();
    	 //System.out.println(ssrVal0);

	}

	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}	
	
}
