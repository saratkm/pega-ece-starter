package starter.stepdefinitions;

import java.io.File;

import org.openqa.selenium.WebDriver;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dataProviders.ConfigFileReader;
import dataProviders.Csv;
import dataProviders.JsonManipulator;
import io.restassured.response.Response;
import managers.CsvParser;
import starter.stepdefinitions.Scenario_Common_StepDefintions;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


//-------- from ATHER ---------------
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

import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import dataProviders.ConfigFileReader;

//------- END IMPORT FROM ATHER -------------

public class Scenario_05XX_StepDefintions {

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
	String bodyscenarioUpdate;
	List<String> row;
	String newJson;	
	String scenarioNum 					= Scenario_Common_StepDefintions.scenarioNumSaved;
	
	
	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
		}
	
	@When("^user reported second earning$")
	public void user_reported_second_earning() throws IOException {
		
	
			newJson = configFileReader.getNewJson(scenarioNum);   //getting json body request as (senarionum)
			CsvParser entries = new CsvParser(configFileReader.getCsvInScenario(scenarioNum));//entries helps us in getting input data
			String earnings1 = entries.getEntry(csvDataInpKey, "Cust_NE_EarningsAmount");//passing second earning to body via input csv
			JsonManipulator a = new JsonManipulator(bodyScn); //"a" helps you to pass test data from input.csv in request
			String add = entries.getEntry(csvDataInpKey, "Cust_HdrAddrLine");
			String countrycode = entries.getEntry(csvDataInpKey, "Cust_Circ_Res_BCC");
			String ssor_request = entries.getEntry(csvDataInpKey, "Cust_SSOR");
			String startdate=entries.getEntry(csvDataInpKey, "Cust_Circ_Educ_Courses_DOE");
			String enddate=entries.getEntry(csvDataInpKey, "Cust_Circ_Educ_Courses_CourseEndDate");
			String benefittype=entries.getEntry(csvDataInpKey,"Cust_Claims_BenefitType");
			a.selectKey("Customer");
			a.putDataEntry("SSOR", ssor_request);
			a.putDataEntry("HdrAddressLine", add);
			a.openArray("Circumstances",0);
			a.selectKey("Residency");
			a.putDataEntry("BirthCountryCode", countrycode);
			a.backTrack(1);
			a.selectKey("Education");
			a.putDataEntry("DateOfEntry", startdate);
			a.openArray("Courses", 0);
			a.putDataEntry("CourseEndDate", enddate);
			a.saveAs(newJson);//new jason is created with your input test data
			String authCookie = (userId + ":" + passwd);
			String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
			String jsonBodyStr = generateStringFromResource(newJson);
			response = RestAssured.given().
			header("Authorization", "Basic "+ authCookieEncoded).	//->working:
			contentType("application/json").//post jason request to retrieve response in below steps.
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
			ResponseBody body = response.getBody();// Retrieve the body of the Response

	}
			@Then("^recalculation occurs$")
			public void recalculation_occurs() {
				ResponseBody body = response.getBody();
				JSONObject jsonObject = new JSONObject();//Grabs the response body and makes a JSONObject of it
				try {
				jsonObject = new JSONObject( body.asString() );
			} catch (JSONException e) {
														// TODO Auto-generated catch block
				e.printStackTrace();
				
			} 
			String nJson =newJson.replaceAll(".json", "_Rsp05.json");//Saves the response as a JSON file
			String filePath = nJson;
			
			JsonManipulator.saveAs(jsonObject, filePath);//Loads JSON file into JsonManipulator class
//			JsonManipulator a = new JsonManipulator(filePath);
			CsvParser response_Entries = new CsvParser(configFileReader.getCsvExpectedScenario(scenarioNum));	
			String ssor =  response_Entries.getEntry(csvDataOutKey, "Cust_SSOR");
			System.out.println("SSOR response excel :"+ssor);
			String startdate = response_Entries.getEntry(csvDataOutKey, "Cust_Circ_Educ_Courses_CourseStartDate");
			String enddate = response_Entries.getEntry(csvDataOutKey, "Cust_Circ_Educ_Courses_CourseEndDate");
			System.out.println("end date excel:" +enddate);
			String benefittypecode = response_Entries.getEntry(csvDataOutKey, "Cust_Claims_BenefitType");
			JsonManipulator aresponse = new JsonManipulator(nJson);//a.response helps you retrieve value from response
			aresponse.selectKey("AssessmentResults");
			aresponse.openArray("Customers", 0);
			aresponse.getDataEntry("HdrNameLine");
			String name_Rsp = aresponse.getDataEntry("HdrNameLine");
			String ssor_Rsp = aresponse.getDataEntry("SSOR");
			aresponse.openArray("PaymentCycles", 0);
			String startDate_Rsp = aresponse.getDataEntry("StartDate");
			aresponse.openArray("Claims", 0);
			String benefittypecode_Rsp = aresponse.getDataEntry("BenefitTypeCode");
			aresponse.backTrack(1);
			System.out.println("SSOR response is :"+ssor_Rsp);
			System.out.println("benefit type code is:" +benefittypecode_Rsp);
			
			Assert.assertTrue(ssor.equals(ssor_Rsp));          //assertion to check expected and response from  testcase_1A
			Assert.assertTrue(benefittypecode.equals(benefittypecode_Rsp));
			
			
	
	}
			@When("^customer will have debt scenario$")
			public void customer_will_have_debt_scenario() throws IOException {

				newJson = configFileReader.getNewJson(scenarioNum);   //getting json body request as (senarionum)
				CsvParser entries1 = new CsvParser(configFileReader.getCsvInScenario(scenarioNum));//entries helps us in getting input data
				String earnings1 = entries1.getEntry(csvDataInpKey, "Cust_NE_EarningsAmount");//passing second earning to body via input csv
				JsonManipulator b = new JsonManipulator(bodyScn); //"a" helps you to pass test data from input.csv in request
				String add1 = entries1.getEntry(csvDataInpKey, "Cust_HdrAddrLine");
				String countrycode1 = entries1.getEntry(csvDataInpKey, "Cust_Circ_Res_BCC");
				String ssor_request1 = entries1.getEntry(csvDataInpKey, "Cust_SSOR");
				String startdate1=entries1.getEntry(csvDataInpKey, "Cust_Circ_Educ_Courses_DOE");
				String enddate1=entries1.getEntry(csvDataInpKey, "Cust_Circ_Educ_Courses_CourseEndDate");
				String benefittype1=entries1.getEntry(csvDataInpKey,"Cust_Claims_BenefitType");
				b.selectKey("Customer");
				b.putDataEntry("SSOR", ssor_request1);
				b.putDataEntry("HdrAddressLine", add1);
				b.openArray("Circumstances",0);
				b.selectKey("Residency");
				b.putDataEntry("BirthCountryCode", countrycode1);
				b.backTrack(1);
				b.selectKey("Education");
				b.putDataEntry("DateOfEntry", startdate1);
				b.openArray("Courses", 0);
				b.putDataEntry("CourseEndDate", enddate1);
				b.saveAs(newJson);//new jason is created with your input test data
				String authCookie = (userId + ":" + passwd);
				String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
				String jsonBodyString = generateStringFromResource(newJson);
				response = RestAssured.given().
				header("Authorization", "Basic "+ authCookieEncoded).	//->working:
				contentType("application/json").//post jason request to retrieve response in below steps.
				body(newJson).
				body(jsonBodyString).
				when().
		        	post(baseURI).
		        then().
		        	statusCode(200).
		        	contentType(ContentType.JSON).
		        	log().all().
		        extract().
		        	response();
				ResponseBody body1 = response.getBody();// Retrieve the body of the Response
			  
			}


			@Then("^re-calculation occurs for payment to be done by user$")
			public void re_calculation_occurs_for_payment_to_be_done_by_user() {
				ResponseBody body1 = response.getBody();
				JSONObject jsonObject = new JSONObject();//Grabs the response body and makes a JSONObject of it
				try {
				jsonObject = new JSONObject( body1.asString() );
			} catch (JSONException e) {
														// TODO Auto-generated catch block
				e.printStackTrace();
				
			} 
			String nJson =newJson.replaceAll(".json", "_Rsp05_B.json");//Saves the response as a JSON file
			String filePath = nJson;
			
			JsonManipulator.saveAs(jsonObject, filePath);//Loads JSON file into JsonManipulator class
//			JsonManipulator a = new JsonManipulator(filePath);
			CsvParser response_Entries1 = new CsvParser(configFileReader.getCsvExpectedScenario(scenarioNum));	
			String ssor1 =  response_Entries1.getEntry(csvDataOutKey, "Cust_SSOR");
			System.out.println("SSOR response excel :"+ssor1);
			String startdate1 = response_Entries1.getEntry(csvDataOutKey, "Cust_Circ_Educ_Courses_CourseStartDate");
			System.out.println("start date excel:" +startdate1);
			String enddate1 = response_Entries1.getEntry(csvDataOutKey, "Cust_Circ_Educ_Courses_CourseEndDate");
			System.out.println("end date excel:" +enddate1);
			String benefittypecode1 = response_Entries1.getEntry(csvDataOutKey, "Cust_Claims_BenefitType");
			JsonManipulator aresponse1 = new JsonManipulator(nJson);//a.response helps you retrieve value from response
			aresponse1.selectKey("AssessmentResults");
			aresponse1.openArray("Customers", 0);
			aresponse1.getDataEntry("HdrNameLine");
			String name_Rsp1 = aresponse1.getDataEntry("HdrNameLine");
			String ssor_Rsp1 = aresponse1.getDataEntry("SSOR");
			aresponse1.openArray("PaymentCycles", 0);
			String startDate_Rsp1 = aresponse1.getDataEntry("StartDate");
			aresponse1.openArray("Claims", 0);
			String benefittypecode_Rsp1 = aresponse1.getDataEntry("BenefitTypeCode");
			aresponse1.backTrack(1);
			System.out.println("SSOR response is :"+ssor_Rsp1);
			System.out.println("start date from second test case:" +startDate_Rsp1);
			System.out.println("benefit type code is:" +benefittypecode_Rsp1);
			
			
			Assert.assertTrue(startdate1.equals(startDate_Rsp1));

			
			}


	}

    




