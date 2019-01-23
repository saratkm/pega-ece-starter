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

	String bodyscenarioUpdated,newJson;
	
	//----------- IMPORT FROM ather START---------
	//WebDriver driver;
	//ConfigFileReader configFileReader;
	//String baseURI, bodyScn, bodyscenarioUpdated, userId, passwd,newJson;
	//File jsonFP=null;
	//Response response = null;
	//----------- IMPORT FROM ather END---------
	
	
	//=== Example code ===//
	@Given("^Shari is currently Receiving FTB_scn5$")
	public void shari_is_currently_Receiving_FTB_scn5() {
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


	
	@Given("^user has first income(.*)$")
	public void user_has_first_income(String arg1) throws IOException {
		configFileReader= new ConfigFileReader();
		baseURI  = configFileReader.getApplicationUrl();		//	"https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
		bodyScn = configFileReader.getBodyScenario("1");         // 	"D:\\DHS\\7JAN\\body_scenario1.json";
		bodyscenarioUpdated = configFileReader.getNewJson();
		userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
		passwd   = configFileReader.getPasswd();				//	"rules";
		jsonFP	 = new File(bodyScn);
		newJson = configFileReader.getNewJson();
		
//		List<List<String>> csvTable = new Csv("C:\\Users\\atheramjed.syed\\Desktop\\5.csv").getCsvTable();
//		Csv.displayCsvTable(csvTable);
		//CsvParser entries = new CsvParser("C:\\Users\\atheramjed.syed\\Desktop\\5.csv");
		CsvParser entries = new CsvParser("D:\\DHS\\7JAN\\csvInScenario_5.csv");
		
		System.out.println(entries.getEntry("userReportedSecondIncome", "Cust_HdrAddrLine"));
          System.out.println("***************");
//  		String add = entries.getEntry("Test01ADependantBDependant", "Cust_HdrAddrLine");
  		String age = entries.getEntry("userReportedSecondIncome", "Cust_DOB");
	
		JsonManipulator a = new JsonManipulator("D:\\DHS\\7JAN\\body_scenario1.json");
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
		
//		System.out.println(a.getCurrentKey());
//		a.backTrack();
//		System.out.println(a.getCurrentKey());
//		JSONObject o = a.getCurrentObject();
//		o = Json.selectKey(o,"Customer");
//		System.out.println(Json.getDataEntry(o, "DOB"));
//a.saveAs("C:\\Users\\can.nguyen\\Desktop\\test2.json");
//		File file = new File("5.csv");
//		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//		for (String line : lines)
//		   { 
//		     String[] array = line.split(",");
//		     System.out.println(array[0]);
//		   }
		

	
	
//========= please move to COMMON function ======	
public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
}
//========= COMMON NOW ======
	
	
	
 @When("^user reported new earning$")
	public void user_reported_new_earning() throws IOException {
	 String authCookie = (userId + ":" + passwd);
 	String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
 	String jsonBodyStr = generateStringFromResource(bodyScn);
 	
		response = RestAssured.given().
				header("Authorization", "Basic "+ authCookieEncoded).	//->working:
				//auth().preemptive().basic(userId, passwd).  			//->working:
				contentType("application/json").
				body(jsonFP).
//				body(jsonBodyStr).
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

	@Then("^recalculation occurs$")
	public void recalculation_occurs() {
	    // Write code here that turns the phrase above into concrete actions
	    
	}
	
	
}
