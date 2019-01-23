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

//------- END IMPORT FROM TIM -------------

public class Scenario_02XX_StepDefintions {

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
	String scenarioNo 					=Scenario_Common_StepDefintions.scenarioNo;
	String configPath 					=Scenario_Common_StepDefintions.configPath;
	CsvParser inputData;
	CsvParser outputData;
	String bodyScn1,authCookieEncoded,jsonBodyStr,basePayloadFilePath, inputCsvFilePath, expectedOutputCsvFilePath, payloadFilePath;

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
		
		payloadFilePath = configFileReader.getNewJson(scenarioNo);
		inputCsvFilePath = configFileReader.getCsvInScenario(scenarioNo);
		expectedOutputCsvFilePath = configFileReader.getCsvExpectedScenario(scenarioNo);
		
		
		//Gets the data from CSV file
		String identifier = csvDataInpKey;
		inputData = new CsvParser(inputCsvFilePath, identifier);
		
		String identifier2 = csvDataOutKey;
		outputData = new CsvParser(expectedOutputCsvFilePath, identifier2);
		
		//Prepares the JSON payload to be sent to the Pega server
		prepareJSONPayload();
		System.out.println("Finished modifying JSON payload with test data from CSV file");
	
	}
	
	@When("^Amy applies for youth allowance with correct values$")
	public void amy_applies_for_youth_allowance_with_correct_values() throws IOException {
		
		
		configFileReader= new ConfigFileReader();
		
    	baseURI  = configFileReader.getApplicationUrl();		//	"https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
    	bodyScn1 = payloadFilePath;        //"D:\\DHS\\7JAN\\body_scenario1.json";
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

    	 // Retrieve the body of the Response
    	 ResponseBody body = response.getBody();
    	 
    	 //Grabs the response body and makes a JSONObject of it
 		 JSONObject jsonObject = new JSONObject();
 		 	 try {
 			 jsonObject = new JSONObject( body.asString() );
 		 } catch (JSONException e) {
  			 e.printStackTrace();
 		 } 
 		 //Saves the response as a JSON file
 		 String filePath = configFileReader.getTmpFilesPath() + "body_UpdatedScenario2.json";
 		 JsonManipulator.saveAs(jsonObject, filePath);
 		 //Loads JSON file into JsonManipulator class
 		 JsonManipulator answer = new JsonManipulator(filePath);
 		 
    	 
    	 //=== STRING CASTING METHODS ====//
    	 // By using the ResponseBody.asString() method, we can convert the  body into the string representation.
    	 //System.out.println("Response Body is: " + body.asString());         View the body
    	 //Assert.assertEquals(body.asString().contains("\"StartDate\":\"2018-02-16\""), true);

    	 answer.selectKey("AssessmentResults");
    	 answer.openArray("Customers",0);
    	 //Assert.assertEquals(csvTable.get(1).get(2),answer.getDataEntry("HdrNameLine"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_SSOR"),answer.getDataEntry("SSOR"));
    	 answer.openArray("PaymentCycles",0);
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_StartDate"),answer.getDataEntry("StartDate"));
    	 answer.openArray("Claims",0);
    	 
    	 // checking YA eligibility
    	 if (statusTest.equals("pass")){
        	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_AMR"),answer.getDataEntry("AMR"));
    	 }
    	 
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_BTC"),answer.getDataEntry("BenefitTypeCode"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Amount"),answer.getDataEntry("Amount").toString().substring(0, outputData.getEntry("AR_Cust_PC_Claims_Amount").length()));
    	 answer.selectKey("Calculations");
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_ParentalIncomeDeduction"),answer.getDataEntry("ParentalIncomeDeduction").toString().substring(0, outputData.getEntry("AR_Cust_PC_Claims_Calculations_ParentalIncomeDeduction").length()));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_IncomeBankOpeningBalance"),answer.getDataEntry("IncomeBankOpeningBalance"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_ParentalIncome"),answer.getDataEntry("ParentalIncome"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_Amount"),answer.getDataEntry("Amount").toString().substring(0, outputData.getEntry("AR_Cust_PC_Claims_Calculations_Amount").length()));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_BasicRate"),answer.getDataEntry("BasicRate"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_IncomeBankCredit"),answer.getDataEntry("IncomeBankCredit"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_IsFulltimeStudent"),answer.getDataEntry("IsFulltimeStudent"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_PersonalIncomeDeductionRateBand1"),answer.getDataEntry("PersonalIncomeDeductionRateBand1"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_PersonalIncomeDeductionRateBand2"),answer.getDataEntry("PersonalIncomeDeductionRateBand2"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_IsEligible"),answer.getDataEntry("IsEligible"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_PersonalIncomeDeduction"),answer.getDataEntry("PersonalIncomeDeduction"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_PersonalIncome"),answer.getDataEntry("PersonalIncome"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_IncomeBankClosingBalance"),answer.getDataEntry("IncomeBankClosingBalance"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_PersonalIncomeDeductionBand1"),answer.getDataEntry("PersonalIncomeDeductionBand1"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_IsAustralianResident"),answer.getDataEntry("IsAustralianResident"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_IsParentalIncomeTestRequired"),answer.getDataEntry("IsParentalIncomeTestRequired"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_PersonalIncomeDeductionBand2"),answer.getDataEntry("PersonalIncomeDeductionBand2"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_ParentalIncomeThreshold"),answer.getDataEntry("ParentalIncomeThreshold"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_Calculations_Age"),answer.getDataEntry("Age"));
    	 answer.backTrack();
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_DOV"),answer.getDataEntry("DOV"));
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_Claims_ReasonCode"),answer.getDataEntry("ReasonCode"));
    	 answer.backTrack();
    	 Assert.assertEquals(outputData.getEntry("AR_Cust_PC_EndDate"),answer.getDataEntry("EndDate"));
    	 answer.backTrack(2);
    	 
    	 // checking cancellation of Parent's FTB
    	 if (statusTest.equals("pass")) {
	    	 answer.openArray("Customers",1);
	    	 answer.openArray("PaymentCycles",0);
	    	 answer.openArray("Claims",0);
	    	 answer.selectKey("Calculations");
	    	 Assert.assertEquals(outputData.getEntry("AR_Cust2_PC_Claims_Calculations_IsEligible"),answer.getDataEntry("IsEligible"));
    	 }
    	 System.out.println("TEST COMPLETED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	public void prepareJSONPayload() {
		JsonManipulator jsonKey = new JsonManipulator(bodyScn);	
		
		jsonKey.putDataEntry("RequestExecutionDate", inputData.getEntry("ReqExecDate"));
		jsonKey.selectKey("Customer");
		jsonKey.putDataEntry("DOB", inputData.getEntry("Cust_DOB"));
		jsonKey.putDataEntry("SSOR", inputData.getEntry("Cust_SSOR"));
		jsonKey.putDataEntry("HdrAddressLine", inputData.getEntry("Cust_HdrAddrLine"));
		jsonKey.selectKey("Address");
		jsonKey.putDataEntry("AddressLine2", inputData.getEntry("Cust_Addr_AddrLine2"));
		jsonKey.putDataEntry("DOE", inputData.getEntry("Cust_Addr_DOE"));
		jsonKey.putDataEntry("Rental2WeAmnt", inputData.getEntry("Cust_Addr_Rental2WeAmnt"));
		jsonKey.backTrack();
		jsonKey.selectKey("MaritalStatus");
		jsonKey.putDataEntry("MaritalStatusCode", inputData.getEntry("Cust_MarStat_MarStatCode"));
		jsonKey.putDataEntry("DOV", inputData.getEntry("Cust_MarStat_DOV"));
		jsonKey.backTrack();
		jsonKey.openArray("Circumstances",0);
		jsonKey.selectKey("Residency");
		jsonKey.putDataEntry("BirthCountryCode", inputData.getEntry("Cust_Circ_Res_BCC"));
		jsonKey.putDataEntry("DateOfResidency", inputData.getEntry("Cust_Circ_Res_DOR"));
		jsonKey.putDataEntry("DateofEntry", inputData.getEntry("Cust_Circ_Res_DOE"));
		jsonKey.openArray("CitizenshipStatus",0);
		jsonKey.putDataEntry("CountryCitizenCode", inputData.getEntry("Cust_Circ_Res_CitStat_CCC"));
		jsonKey.putDataEntry("DateOfEntry", inputData.getEntry("Cust_Circ_Res_CitStat_DOE"));
		jsonKey.backTrack();
		jsonKey.selectKey("Visa");
		jsonKey.putDataEntry("GrantDate", inputData.getEntry("Cust_Circ_Visa_GrantDate"));
		jsonKey.putDataEntry("VisaNumber", inputData.getEntry("Cust_Circ_Visa_VisaNo"));
		jsonKey.putDataEntry("VisaSubclassCode", inputData.getEntry("Cust_Circ_Visa_VisaSubclassCode"));
		jsonKey.backTrack(2);
		jsonKey.selectKey("Education");
		jsonKey.putDataEntry("EducationLevel", inputData.getEntry("Cust_Circ_Educ_EducLevel"));
		jsonKey.putDataEntry("StudentStatusCode", inputData.getEntry("Cust_Circ_Educ_StudentStatusCode"));
		jsonKey.openArray("Courses",0);
		jsonKey.putDataEntry("CourseStartDate", inputData.getEntry("Cust_Circ_Educ_Courses_CourseStartDate"));
		jsonKey.putDataEntry("CourseEndDate", inputData.getEntry("Cust_Circ_Educ_Courses_CourseEndDate"));
		jsonKey.putDataEntry("DateOfEntry", inputData.getEntry("Cust_Circ_Educ_Courses_DOE"));
		jsonKey.putDataEntry("StudentParticipationStatus", inputData.getEntry("Cust_Circ_Educ_Courses_StudentParticipationStatus"));
		jsonKey.backTrack(3);
		//jsonKey.backTrack(2);
		//No earnings
		// uses 25,26,27,28
		jsonKey.openArray("Claims",0);
		jsonKey.putDataEntry("BenefitTypeCode", inputData.getEntry("Cust_Claims_BenefitType"));
		jsonKey.putDataEntry("DOV", inputData.getEntry("Cust_Claims_DOV"));
		jsonKey.backTrack(2);
		//Parent 1
		jsonKey.openArray("Parents",0);
		jsonKey.openArray("Kids",0);
		jsonKey.putDataEntry("SSR", inputData.getEntry("P1_Kids_SSR"));
		jsonKey.openArray("Claims",0);
		jsonKey.putDataEntry("BenefitTypeCode", inputData.getEntry("P1_Kids_Claims_BenefitType"));
		jsonKey.putDataEntry("DOV", inputData.getEntry("P1_Kids_Claims_DOV"));
		//No earnings for parent 1
		// jsonKey.backTrack(2);
		// uses 34,35,36,37
		jsonKey.backTrack(3);
		jsonKey.openArray("Parents",1);
		jsonKey.openArray("Kids",0);
		jsonKey.putDataEntry("SSR", inputData.getEntry("P2_Kids_SSR"));
		//no claims for parent 2
		// uses 39,40
		jsonKey.backTrack();
		jsonKey.openArray("Circumstances",0);
		jsonKey.openArray("Earnings",0);
		jsonKey.putDataEntry("IncomeFreqCode", inputData.getEntry("P2_Circ_Earn_IncomeFreq"));
		jsonKey.putDataEntry("DateOfVerification", inputData.getEntry("P2_Circ_Earn_DOV"));
		jsonKey.putDataEntry("EarningsAmount", inputData.getEntry("P2_Circ_Earn_EarnAmt"));
		jsonKey.backTrack();
		jsonKey.openArray("Earnings",1);
		jsonKey.putDataEntry("IncomeFreqCode", inputData.getEntry("P2_Circ_Earn_IncomeFreq2"));
		jsonKey.putDataEntry("DateOfVerification", inputData.getEntry("P2_Circ_Earn_DOV2"));
		jsonKey.putDataEntry("EarningsAmount", inputData.getEntry("P2_Circ_Earn_EarnAmt2"));
		jsonKey.backTrack();
		jsonKey.openArray("Earnings",2);
		jsonKey.putDataEntry("IncomeFreqCode", inputData.getEntry("P2_Circ_Earn_IncomeFreq3"));
		jsonKey.putDataEntry("DateOfVerification", inputData.getEntry("P2_Circ_Earn_DOV3"));
		jsonKey.putDataEntry("EarningsAmount", inputData.getEntry("P2_Circ_Earn_EarnAmt3"));
		
		jsonKey.saveAs(payloadFilePath);
	}
	
}
