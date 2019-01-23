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

//import library.*;
import dataProviders.CsvForScnOne;

/*
import dataConversion.*;
*/
//------- END IMPORT FROM TIM -------------

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
	String csvDataInpKey 				= Scenario_Common_StepDefintions.csvDataInpKey;
	String csvDataOutKey 				= Scenario_Common_StepDefintions.csvDataOutKey;

	String bodyScn1,authCookieEncoded,jsonBodyStr;

	//----------- IMPORT FROM CAN START---------
	ResponseBody body;
	List<String> row;
	//----------- IMPORT FROM CAN END---------
	
	
	//=== Example code ===//
	
	@Given("^ShariIs currently Receiving FTBfor Scenario One$")
	public void shariIs_currently_Receiving_FTBfor_Scenario_One() {
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
	
	//========== example code ============

	

	@Given("^she lives at home with her parents, Lando and Shari$")
	public void she_lives_at_home_with_her_parents_Lando_and_Shari() throws IOException {
		//Gets the data from CSV file
		//List<List<String>> csvTable = new Csv("C:\\Users\\can.nguyen\\Desktop\\TestScenario -1 - Can.csv").getCsvTable();
		List<List<String>> csvTable = new CsvForScnOne("D:\\DHS\\7JAN\\csvInScenario_1.csv").getCsvTable();
		
		
		//Csv.displayCsvTable(csvTable);
		
		//Gets a specific row from the CSV data except for the first 2 columns based on a unique identifying string in the second column
		String identifier = "amyApplies";
		row = CsvForScnOne.getRow(csvTable, identifier);
		for (int i = 0; i < row.size();i++) {
			System.out.print(row.get(i) + ", ");
		}
		
		//Prepares the JSON payload to be sent to the Pega server
		prepareJSONPayload();
		
		configFileReader= new ConfigFileReader();
		
    	baseURI  = configFileReader.getApplicationUrl();		//	"https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
    	bodyScn1 = configFileReader.getBodyScenario("1");  		// 	"D:\\DHS\\7JAN\\body_scenario1.json";
    	userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
    	baseURI = "https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/CLM_YAL";
    	bodyScn1 = "C:\\Users\\can.nguyen\\Desktop\\ScenarioPayload.json";
    	passwd   = configFileReader.getPasswd();				//	"rules";
    	jsonFP	 = new File(bodyScn1);
    	
    	//JsonManipulator scn1 = new JsonManipulator("C:\\Users\\can.nguyen\\Desktop\\Scenario1.json");
    	
	}
	
	@When("^(.*) for youth allowance$")
	public void for_youth_allowance(String arg1) throws IOException {
		//Setup for logging into Pega
		String authCookie = (userId + ":" + passwd);
    	String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
    	
		String jsonBodyStr = generateStringFromResource(bodyScn1);
		
    	//Posting to Pega and getting Response
    	response = RestAssured.
    		given().header("Authorization", "Basic "+ authCookieEncoded).contentType("application/json").body(jsonFP).
    		when().post(baseURI).
            then().statusCode(200).contentType(ContentType.JSON).log().all().extract().response();

    	
	}

	@Then("^Calculate (.*) for Amy$")
	public void calculate_for_Amy(String arg1) {
		System.out.println("Status Code  :"+ response.statusCode());
    	System.out.println("Content Type :"+ response.contentType());
    	
    	
		// Retrieve the body of the Response
		ResponseBody body = response.getBody();
		 
		//=== STRING CASTING METHODS ====//
		// By using the ResponseBody.asString() method, we can convert the  body into the string representation.
		System.out.println("Response Body is: " + body.asString());
		 
		//Grabs the response body and makes a JSONObject of it
		JSONObject jsonObject = new JSONObject();
			try {
			jsonObject = new JSONObject( body.asString() );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//Saves the response as a JSON file
		String filePath = "C:\\Users\\can.nguyen\\Desktop\\response.json";
		JsonManipulator.saveAs(jsonObject, filePath);
		//Loads JSON file into JsonManipulator class
		JsonManipulator a = new JsonManipulator(filePath);
		
		a.selectKey("AssessmentResults");
		a.openArray("Customers",0);
		a.openArray("PaymentCycles",0);
		a.openArray("Claims",0);
		a.selectKey("Calculations");
		System.out.println(a.getDataEntry("Amount"));
		Assert.assertEquals(body.asString().contains("\"StartDate\":\"2018-02-16\""), true);
    	 
    	 
		//=== JSON CASTING METHODS ==//
		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();
		ArrayList SSOR = jsonPathEvaluator.get("AssessmentResults.Customers.SSOR");
		String ssrVal0 = SSOR.get(0).toString();
		 
		System.out.println("SSOR received from Response " + SSOR);
		Assert.assertEquals ("Correct SSOR received in the Response", "204794278", ssrVal0); // (msg, expected, actual)
	    
	}
	
	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	public void prepareJSONPayload() {
		JsonManipulator jsonKey = new JsonManipulator("C:\\Users\\can.nguyen\\Desktop\\Scenario1.json");
		
		
		jsonKey.putDataEntry("RequestExecutionDate", row.get(0));
		jsonKey.selectKey("Customer");
		jsonKey.putDataEntry("DOB", row.get(1));
		jsonKey.putDataEntry("SSOR", row.get(2));
		jsonKey.putDataEntry("HdrAddressLine", row.get(3));
		jsonKey.selectKey("Address");
		jsonKey.putDataEntry("AddressLine2", row.get(4));
		jsonKey.putDataEntry("DOE", row.get(5));
		System.out.println("AAAAAAAAAAAAAAAAAAAAAA" + row.get(6));
		jsonKey.putDataEntry("Rental2WeAmnt", row.get(6));
		jsonKey.backTrack();
		jsonKey.selectKey("MaritalStatus");
		jsonKey.putDataEntry("MaritalStatusCode", row.get(7));
		jsonKey.putDataEntry("DOV", row.get(8));
		jsonKey.backTrack();
		jsonKey.openArray("Circumstances",0);
		jsonKey.selectKey("Residency");
		jsonKey.putDataEntry("BirthCountryCode", row.get(9));
		jsonKey.putDataEntry("DateOfResidency", row.get(10));
		jsonKey.putDataEntry("DateofEntry", row.get(11));
		jsonKey.openArray("CitizenshipStatus",0);
		jsonKey.putDataEntry("CountryCitizenCode", row.get(12));
		jsonKey.putDataEntry("DateOfEntry", row.get(13));
		jsonKey.backTrack();
		jsonKey.selectKey("Visa");
		jsonKey.putDataEntry("GrantDate", row.get(14));
		jsonKey.putDataEntry("VisaNumber", row.get(15));
		jsonKey.putDataEntry("VisaSubclassCode", row.get(16));
		jsonKey.backTrack(2);
		jsonKey.selectKey("Education");
		jsonKey.putDataEntry("EducationLevel", row.get(17));
		jsonKey.putDataEntry("StudentStatusCode", row.get(18));
		jsonKey.openArray("Courses",0);
		jsonKey.putDataEntry("CourseStartDate", row.get(19));
		jsonKey.putDataEntry("CourseEndDate", row.get(20));
		jsonKey.putDataEntry("DateOfEntry", row.get(21));
		jsonKey.putDataEntry("StudentParticipationStatus", row.get(22));
		jsonKey.backTrack(3);
		//jsonKey.backTrack(2);
		//No earnings
		// uses 25,26,27,28
		jsonKey.openArray("Claims",0);
		jsonKey.putDataEntry("BenefitTypeCode", row.get(27));
		jsonKey.putDataEntry("DOV", row.get(28));
		jsonKey.backTrack(2);
		//Parent 1
		jsonKey.openArray("Parents",0);
		jsonKey.openArray("Kids",0);
		jsonKey.putDataEntry("SSR", row.get(29));
		jsonKey.openArray("Claims",0);
		jsonKey.putDataEntry("BenefitTypeCode", row.get(30));
		jsonKey.putDataEntry("DOV", row.get(31));
		//No earnings for parent 1
		// jsonKey.backTrack(2);
		// uses 34,35,36,37
		jsonKey.backTrack(3);
		jsonKey.openArray("Parents",1);
		jsonKey.openArray("Kids",0);
		jsonKey.putDataEntry("SSR", row.get(36));
		//no claims for parent 2
		// uses 39,40
		jsonKey.backTrack();
		jsonKey.openArray("Circumstances",0);
		jsonKey.openArray("Earnings",0);
		jsonKey.putDataEntry("IncomeFreqCode", row.get(39));
		jsonKey.putDataEntry("DateOfVerification", row.get(40));
		jsonKey.putDataEntry("EarningsAmount", row.get(41));
		
		jsonKey.saveAs("C:\\Users\\can.nguyen\\Desktop\\ScenarioPayload.json");
	}
	
	
	
	
}
