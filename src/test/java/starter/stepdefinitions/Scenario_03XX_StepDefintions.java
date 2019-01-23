package starter.stepdefinitions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dataProviders.ConfigFileReader;
import dataProviders.Csv;
import dataProviders.JsonManipulator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import managers.CsvParser;
import starter.stepdefinitions.Scenario_Common_StepDefintions;

import org.json.JSONObject;
import org.junit.Assert;
import org.apache.commons.net.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;



public class Scenario_03XX_StepDefintions {

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
	HashMap<String,String> elementsMap 				= Scenario_Common_StepDefintions.elementsMap;
	


	@Given("^Customer is already receiving youth allowance and income history is reported$")
	public void customer_is_already_receiving_youth_allowance_and_income_history_is_reported() throws JSONException {
		jsonDataHndl.updateRequestBody(csvInpData, csvDataInpKey, elementsMap);	

		// Save the modified request
		jsonDataHndl.saveAs(configFileReader.getTmpFilesPath() + "body_UpdatedScenario3_" + csvInpData.getEntry("TestCaseNo") + ".json");

	}

	@When("^Youth allowance is calculated based on income history$")
	public void youth_allowance_is_calculated_based_on_income_history() {
		// Read the modified request to be passed
		bodyScn = configFileReader.getTmpFilesPath() + "body_UpdatedScenario3_" + csvInpData.getEntry("TestCaseNo") + ".json";
		userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
		passwd   = configFileReader.getPasswd();				//	"rules";
		jsonFP	 = new File(bodyScn);
		String authCookie = (userId + ":" + passwd);
		String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));

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
	}

	@Then("^Check if youth allowance calculation is correct based on income history$")
	public void check_if_youth_allowance_calculation_is_correct_based_on_income_history() throws JSONException {
		// Retrieve the body of the Response
		ResponseBody body = response.getBody();

		//Grabs the response body and makes a JSONObject of it
		JSONObject responseJson = new JSONObject();
		responseJson = new JSONObject(body.asString());

		//Saves the response as a JSON file
		String filePath = configFileReader.getTmpFilesPath() + "body_ResponseScenario3_" + csvInpData.getEntry("TestCaseNo") + ".json";
		JsonManipulator.saveAs(responseJson, filePath);

		//Loads JSON file into JsonManipulator class
		jsonDataHndl = new JsonManipulator(filePath);

		csvExpData.loadRow(csvDataOutKey);

		//Check results
		jsonDataHndl.selectKey("AssessmentResults");
		jsonDataHndl.openArray("Customers", 0);
		jsonDataHndl.openArray("PaymentCycles", 0);
//		Assert.assertEquals(csvExpData.getEntry("PC1_StartDate"),jsonDataHndl.getDataEntry("StartDate"));
//		Assert.assertEquals(csvExpData.getEntry("PC1_EndDate"),jsonDataHndl.getDataEntry("EndDate"));

		//Check first claim
		jsonDataHndl.openArray("Claims", 0);
//		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Amt"),jsonDataHndl.getDataEntry("Amount"));
//		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_DOV"),jsonDataHndl.getDataEntry("DOV"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_BenefitTypeCode"),jsonDataHndl.getDataEntry("BenefitTypeCode"));

		//Check first claim calculations
		jsonDataHndl.selectKey("Calculations");
//		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_ParentalIncomeDeduction"),jsonDataHndl.getDataEntry("ParentalIncomeDeduction"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IncomeBankOpeningBalance"),jsonDataHndl.getDataEntry("IncomeBankOpeningBalance"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_ParentalIncome"),jsonDataHndl.getDataEntry("ParentalIncome"));
//		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_Amount"),jsonDataHndl.getDataEntry("Amount"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_BasicRate"),jsonDataHndl.getDataEntry("BasicRate"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_BankCredit"),jsonDataHndl.getDataEntry("IncomeBankCredit"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsFulltimeStudent"),jsonDataHndl.getDataEntry("IsFulltimeStudent"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionRateBand1"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionRateBand1"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionRateBand2"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionRateBand2"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsEligible"),jsonDataHndl.getDataEntry("IsEligible"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeduction"),jsonDataHndl.getDataEntry("PersonalIncomeDeduction"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncome"),jsonDataHndl.getDataEntry("PersonalIncome"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IncomeBankClosingBalance"),jsonDataHndl.getDataEntry("IncomeBankClosingBalance"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionBand1"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionBand1"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsAustralianResident"),jsonDataHndl.getDataEntry("IsAustralianResident"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsParentalIncomeTestRequired"),jsonDataHndl.getDataEntry("IsParentalIncomeTestRequired"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionBand2"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionBand2"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_ParentalIncomeThreshold"),jsonDataHndl.getDataEntry("ParentalIncomeThreshold"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_Age"),jsonDataHndl.getDataEntry("Age"));

	}

	@Given("^Customer moved to a new place to live$")
	public void customer_moved_to_a_new_place_to_live() throws JSONException {
		jsonDataHndl.updateRequestBody(csvInpData, csvDataInpKey, elementsMap);	

		// Save the modified request
		jsonDataHndl.saveAs(configFileReader.getTmpFilesPath() + "body_UpdatedScenario3_" + csvInpData.getEntry("TestCaseNo") + ".json");

	}

	@When("^Youth allowance is calculated based on change of dependency and income history$")
	public void youth_allowance_is_calculated_based_on_change_of_dependency_and_income_history() {
		// Read the modified request to be passed
		bodyScn = configFileReader.getTmpFilesPath() + "body_UpdatedScenario3_" + csvInpData.getEntry("TestCaseNo") + ".json";
		userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
		passwd   = configFileReader.getPasswd();				//	"rules";
		jsonFP	 = new File(bodyScn);
		String authCookie = (userId + ":" + passwd);
		String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));

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

	}

	@Then("^Check if youth allowance calculation is correct based on change of dependency and income history$")
	public void check_if_youth_allowance_calculation_is_correct_based_on_change_of_dependency_and_income_history() throws JSONException {
		// Retrieve the body of the Response
		ResponseBody body = response.getBody();

		//Grabs the response body and makes a JSONObject of it
		JSONObject responseJson = new JSONObject();
		responseJson = new JSONObject(body.asString());

		//Saves the response as a JSON file
		String filePath = configFileReader.getTmpFilesPath() + "body_ResponseScenario3_" + csvInpData.getEntry("TestCaseNo") + ".json";
		JsonManipulator.saveAs(responseJson, filePath);

		//Loads JSON file into JsonManipulator class
		jsonDataHndl = new JsonManipulator(filePath);

		csvExpData.loadRow(csvDataOutKey);

		//Check results
		jsonDataHndl.selectKey("AssessmentResults");
		jsonDataHndl.openArray("Customers", 0);
		jsonDataHndl.openArray("PaymentCycles", 0);
		Assert.assertEquals(csvExpData.getEntry("PC1_StartDate"),jsonDataHndl.getDataEntry("StartDate"));
		Assert.assertEquals(csvExpData.getEntry("PC1_EndDate"),jsonDataHndl.getDataEntry("EndDate"));

		//Check first claim
		jsonDataHndl.openArray("Claims", 0);
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Amt"),jsonDataHndl.getDataEntry("Amount"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_DOV"),jsonDataHndl.getDataEntry("DOV"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_BenefitTypeCode"),jsonDataHndl.getDataEntry("BenefitTypeCode"));

		//Check first claim calculations
		jsonDataHndl.selectKey("Calculations");
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_ParentalIncomeDeduction"),jsonDataHndl.getDataEntry("ParentalIncomeDeduction"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IncomeBankOpeningBalance"),jsonDataHndl.getDataEntry("IncomeBankOpeningBalance"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_ParentalIncome"),jsonDataHndl.getDataEntry("ParentalIncome"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_Amount"),jsonDataHndl.getDataEntry("Amount"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_BasicRate"),jsonDataHndl.getDataEntry("BasicRate"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_BankCredit"),jsonDataHndl.getDataEntry("IncomeBankCredit"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsFulltimeStudent"),jsonDataHndl.getDataEntry("IsFulltimeStudent"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionRateBand1"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionRateBand1"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionRateBand2"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionRateBand2"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsEligible"),jsonDataHndl.getDataEntry("IsEligible"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeduction"),jsonDataHndl.getDataEntry("PersonalIncomeDeduction"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncome"),jsonDataHndl.getDataEntry("PersonalIncome"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IncomeBankClosingBalance"),jsonDataHndl.getDataEntry("IncomeBankClosingBalance"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionBand1"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionBand1"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsAustralianResident"),jsonDataHndl.getDataEntry("IsAustralianResident"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_IsParentalIncomeTestRequired"),jsonDataHndl.getDataEntry("IsParentalIncomeTestRequired"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_PersonalIncomeDeductionBand2"),jsonDataHndl.getDataEntry("PersonalIncomeDeductionBand2"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_ParentalIncomeThreshold"),jsonDataHndl.getDataEntry("ParentalIncomeThreshold"));
		Assert.assertEquals(csvExpData.getEntry("PC1_Clm_Calc_Age"),jsonDataHndl.getDataEntry("Age"));

	}

//	public void updateRequestBody() throws JSONException {
//		int i;
//		jsonDataHndl.goToRoot();
//		csvInpData.loadRow(csvDataInpKey);
//
//		//Update customer details
//		jsonDataHndl.putDataEntry("RequestExecutionDate", csvInpData.getEntry("ReqExecDate"));
//		jsonDataHndl.selectKey("Customer");
//		jsonDataHndl.putDataEntry("DOB", csvInpData.getEntry("Cust_DOB"));
//		jsonDataHndl.putDataEntry("SSOR", csvInpData.getEntry("Cust_SSOR"));
//
//		//Update address details
//		jsonDataHndl.putDataEntry("HdrAddressLine", csvInpData.getEntry("Cust_HdrAddrLine"));
//		jsonDataHndl.selectKey("Address");
//		jsonDataHndl.putDataEntry("AddressLine2", csvInpData.getEntry("Cust_Addr_AddrLine2"));
//		jsonDataHndl.putDataEntry("DOE", csvInpData.getEntry("Cust_Addr_DOE"));
//		jsonDataHndl.putDataEntry("Rental2WeAmnt", csvInpData.getEntry("Cust_Addr_Rental2WeAmnt"));		
//		jsonDataHndl.backTrack();	//Back to Customer
//
//		//Update marital status
//		jsonDataHndl.selectKey("MaritalStatus");
//		jsonDataHndl.putDataEntry("MaritalStatusCode", csvInpData.getEntry("Cust_MarStat_MarStatCode"));
//		jsonDataHndl.putDataEntry("DOV", csvInpData.getEntry("Cust_MarStat_DOV"));	
//		jsonDataHndl.backTrack();	//Back to Customer
//
//		jsonDataHndl.openArray("Circumstances", 0);
//
//		//Update residency details
//		jsonDataHndl.selectKey("Residency");
//		jsonDataHndl.putDataEntry("BirthCountryCode", csvInpData.getEntry("Cust_Circ_Res_BCC"));
//		jsonDataHndl.putDataEntry("DateOfResidency", csvInpData.getEntry("Cust_Circ_Res_DOR"));
//		jsonDataHndl.putDataEntry("DateofEntry", csvInpData.getEntry("Cust_Circ_Res_DOE"));
//		jsonDataHndl.openArray("CitizenshipStatus", 0);
//		jsonDataHndl.putDataEntry("CountryCitizenCode", csvInpData.getEntry("Cust_Circ_Res_CitStat_CCC"));
//		jsonDataHndl.putDataEntry("DateOfEntry", csvInpData.getEntry("Cust_Circ_Res_CitStat_DOE"));
//		jsonDataHndl.backTrack();	//Back to Residency
//		jsonDataHndl.selectKey("Visa");
//		jsonDataHndl.putDataEntry("GrantDate", csvInpData.getEntry("Cust_Circ_Visa_GrantDate"));
//		jsonDataHndl.putDataEntry("VisaNumber", csvInpData.getEntry("Cust_Circ_Visa_VisaNo"));
//		jsonDataHndl.putDataEntry("VisaSubclassCode", csvInpData.getEntry("Cust_Circ_Visa_VisaSubclassCode"));
//		jsonDataHndl.backTrack(2);	//Back to Circumstances
//
//		//Update education details
//		jsonDataHndl.selectKey("Education");
//		jsonDataHndl.putDataEntry("EducationLevel", csvInpData.getEntry("Cust_Circ_Educ_EducLevel"));	
//		jsonDataHndl.putDataEntry("StudentStatusCode", csvInpData.getEntry("Cust_Circ_Educ_StudentStatusCode"));
//		jsonDataHndl.openArray("Courses", 0);
//		jsonDataHndl.putDataEntry("CourseStartDate", csvInpData.getEntry("Cust_Circ_Educ_Courses_CourseStartDate"));
//		jsonDataHndl.putDataEntry("CourseEndDate", csvInpData.getEntry("Cust_Circ_Educ_Courses_CourseEndDate"));
//		jsonDataHndl.putDataEntry("DateOfEntry", csvInpData.getEntry("Cust_Circ_Educ_Courses_DOE"));
//		jsonDataHndl.putDataEntry("StudentParticipationStatus", csvInpData.getEntry("Cust_Circ_Educ_Courses_StudentParticipationStatus"));
//		jsonDataHndl.backTrack(2);	//Back to Circumstances
//
//		//Update income bank credits
//		jsonDataHndl.selectKey("IncomeBank");
//		jsonDataHndl.putDataEntry("Credits", csvInpData.getEntry("Cust_Circ_IncomeBank_Credits"));	
//		jsonDataHndl.backTrack();	//Back to Circumstances
//
//		//Create customer earning history
//		i = 1;
//		while(csvInpData.isColumnKeyExists("Cust_Circ_Earn_Id_" + i) &&
//				csvInpData.getEntry("Cust_Circ_Earn_Id_" + i) != null &&
//				!csvInpData.getEntry("Cust_Circ_Earn_Id_" + i).trim().isEmpty()) {
//			JSONObject newEarning = new JSONObject();
//			newEarning.put("id", csvInpData.getEntry("Cust_Circ_Earn_Id_" + i));
//			newEarning.put("AMR", csvInpData.getEntry("Cust_Circ_Earn_AMR_" + i));
//			newEarning.put("IncomeFreqCode", csvInpData.getEntry("Cust_Circ_Earn_IncomeFreq_" + i));
//			newEarning.put("AverageInd", csvInpData.getEntry("Cust_Circ_Earn_AverageInd_" + i));
//			newEarning.put("AverageTypeCode", csvInpData.getEntry("Cust_Circ_Earn_AverageTypeCode_" + i));
//			newEarning.put("ChangeReasonCode", csvInpData.getEntry("Cust_Circ_Earn_ChangeReasonCode_" + i));
//			newEarning.put("ChannelCode", csvInpData.getEntry("Cust_Circ_Earn_ChannelCode_" + i));
//			newEarning.put("DateOfVerification", csvInpData.getEntry("Cust_Circ_Earn_DOV_" + i));
//			newEarning.put("EarningsAmount", csvInpData.getEntry("Cust_Circ_Earn_EarnAmt_" + i));
//			newEarning.put("EarningsVerificationCode", csvInpData.getEntry("Cust_Circ_Earn_EarnVerificationCode_" + i));
//			newEarning.put("EmploymentStatusCode", csvInpData.getEntry("Cust_Circ_Earn_EmploymentStatusCode_" + i));
//			newEarning.put("HrPerFNNum", csvInpData.getEntry("Cust_Circ_Earn_HrPerFNNum_" + i));
//			newEarning.put("UniqueID", csvInpData.getEntry("Cust_Circ_Earn_UniqueID_" + i));		
//			jsonDataHndl.putDataEntryForArray("Earnings", newEarning);
//			i++;
//		}
//		jsonDataHndl.backTrack();	//Back to Customer
//
//		//Update customer claims details
//		jsonDataHndl.openArray("Claims", 0);
//		jsonDataHndl.putDataEntry("BenefitTypeCode", csvInpData.getEntry("Cust_Claims_BenefitType"));	
//		jsonDataHndl.putDataEntry("DOV", csvInpData.getEntry("Cust_Claims_DOV"));	
//		jsonDataHndl.goToRoot();
//
//		//Update parent 1 details
//		jsonDataHndl.openArray("Parents", 0);
//		jsonDataHndl.putDataEntry("HdrAddressLine", csvInpData.getEntry("P1_HdrAddrLine"));				
//
//		//Create parent 1 earning history
//		jsonDataHndl.openArray("Circumstances", 0);	
//		i = 1;
//		while(csvInpData.isColumnKeyExists("P1_Circ_Earn_Id_" + i) &&
//				csvInpData.getEntry("P1_Circ_Earn_Id_" + i) != null &&
//				!csvInpData.getEntry("P1_Circ_Earn_Id_" + i).trim().isEmpty()) {
//			JSONObject newEarning = new JSONObject();
//			newEarning.put("id", csvInpData.getEntry("P1_Circ_Earn_Id_" + i));
//			newEarning.put("AMR", csvInpData.getEntry("P1_Circ_Earn_AMR_" + i));
//			newEarning.put("IncomeFreqCode", csvInpData.getEntry("P1_Circ_Earn_IncomeFreq_" + i));
//			newEarning.put("AverageInd", csvInpData.getEntry("P1_Circ_Earn_AverageInd_" + i));
//			newEarning.put("AverageTypeCode", csvInpData.getEntry("P1_Circ_Earn_AverageTypeCode_" + i));
//			newEarning.put("ChangeReasonCode", csvInpData.getEntry("P1_Circ_Earn_ChangeReasonCode_" + i));
//			newEarning.put("ChannelCode", csvInpData.getEntry("P1_Circ_Earn_ChannelCode_" + i));
//			newEarning.put("DateOfVerification", csvInpData.getEntry("P1_Circ_Earn_DOV_" + i));
//			newEarning.put("EarningsAmount", csvInpData.getEntry("P1_Circ_Earn_EarnAmt_" + i));
//			newEarning.put("EarningsVerificationCode", csvInpData.getEntry("P1_Circ_Earn_EarnVerificationCode_" + i));
//			newEarning.put("EmploymentStatusCode", csvInpData.getEntry("P1_Circ_Earn_EmploymentStatusCode_" + i));
//			newEarning.put("HrPerFNNum", csvInpData.getEntry("P1_Circ_Earn_HrPerFNNum_" + i));
//			newEarning.put("UniqueID", csvInpData.getEntry("P1_Circ_Earn_UniqueID_" + i));		
//			jsonDataHndl.putDataEntryForArray("Earnings", newEarning);
//			i++;
//		}
//		jsonDataHndl.backTrack();	//Back to Parent 1
//
//		//Update parent 1 kids' details
//		jsonDataHndl.openArray("Kids", 0);
//		jsonDataHndl.putDataEntry("SSR", csvInpData.getEntry("P1_Kids_SSR"));
//		jsonDataHndl.openArray("Claims", 0);
//		jsonDataHndl.putDataEntry("BenefitTypeCode", csvInpData.getEntry("P1_Kids_Claims_BenefitType"));
//		jsonDataHndl.putDataEntry("DOV", csvInpData.getEntry("P1_Kids_Claims_DOV"));	
//		jsonDataHndl.goToRoot();
//
//		//Update parent 2 details
//		jsonDataHndl.openArray("Parents", 1);
//		jsonDataHndl.putDataEntry("HdrAddressLine", csvInpData.getEntry("P2_HdrAddrLine"));	
//
//		//Create parent 2 earning history
//		jsonDataHndl.openArray("Circumstances", 0);	
//		i = 1;
//		while(csvInpData.isColumnKeyExists("P2_Circ_Earn_Id_" + i) &&
//				csvInpData.getEntry("P2_Circ_Earn_Id_" + i) != null &&
//				!csvInpData.getEntry("P2_Circ_Earn_Id_" + i).trim().isEmpty()) {
//			JSONObject newEarning = new JSONObject();
//			newEarning.put("id", csvInpData.getEntry("P2_Circ_Earn_Id_" + i));
//			newEarning.put("AMR", csvInpData.getEntry("P2_Circ_Earn_AMR_" + i));
//			newEarning.put("IncomeFreqCode", csvInpData.getEntry("P2_Circ_Earn_IncomeFreq_" + i));
//			newEarning.put("AverageInd", csvInpData.getEntry("P2_Circ_Earn_AverageInd_" + i));
//			newEarning.put("AverageTypeCode", csvInpData.getEntry("P2_Circ_Earn_AverageTypeCode_" + i));
//			newEarning.put("ChangeReasonCode", csvInpData.getEntry("P2_Circ_Earn_ChangeReasonCode_" + i));
//			newEarning.put("ChannelCode", csvInpData.getEntry("P2_Circ_Earn_ChannelCode_" + i));
//			newEarning.put("DateOfVerification", csvInpData.getEntry("P2_Circ_Earn_DOV_" + i));
//			newEarning.put("EarningsAmount", csvInpData.getEntry("P2_Circ_Earn_EarnAmt_" + i));
//			newEarning.put("EarningsVerificationCode", csvInpData.getEntry("P2_Circ_Earn_EarnVerificationCode_" + i));
//			newEarning.put("EmploymentStatusCode", csvInpData.getEntry("P2_Circ_Earn_EmploymentStatusCode_" + i));
//			newEarning.put("HrPerFNNum", csvInpData.getEntry("P2_Circ_Earn_HrPerFNNum_" + i));
//			newEarning.put("UniqueID", csvInpData.getEntry("P2_Circ_Earn_UniqueID_" + i));		
//			jsonDataHndl.putDataEntryForArray("Earnings", newEarning);
//			i++;
//		}
//		jsonDataHndl.backTrack();	//Back to Parent 2
//
//		//Update parent 1 kids' details
//		jsonDataHndl.openArray("Kids", 0);
//		jsonDataHndl.putDataEntry("SSR", csvInpData.getEntry("P2_Kids_SSR"));
//		jsonDataHndl.goToRoot();		
//
//	}
	

}
