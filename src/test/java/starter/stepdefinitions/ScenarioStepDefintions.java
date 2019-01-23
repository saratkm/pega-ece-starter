package starter.stepdefinitions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import dataProviders.ConfigFileReader;


public class ScenarioStepDefintions {

	WebDriver driver;
	ConfigFileReader configFileReader;
	String baseURI, bodyScn, userId, passwd;
	File jsonFP=null;
	Response response = null;

	@Given("^user initiatiates scenario One$")
	public void user_initiatiates_scenario_One() throws JSONException, IOException{
		configFileReader= new ConfigFileReader();

		baseURI  = configFileReader.getApplicationUrl();		//	"https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
		bodyScn = configFileReader.getBodyScenario("1");  		// 	"D:\\DHS\\7JAN\\body_scenario1.json";
		userId   = configFileReader.getUserId();				//	"seri.charoensri@pega.com";
		passwd   = configFileReader.getPasswd();				//	"rules";
		jsonFP	 = new File(bodyScn);

		// ********** Testing of Updating JSON file ********** //
		JSONObject json = convertJsonFileToObject(bodyScn);
		
		//To get JSONObject within JSONObject
		JSONObject cust = json.getJSONObject("Customer");
		
		//To get JSONArray within JSONObject
		JSONObject cust_circumstances = cust.getJSONArray("Circumstances").getJSONObject(0);
		
		JSONObject cust_circumstances_residency = cust_circumstances.getJSONObject("Residency");
		JSONObject cust_circumstances_residency_citizenshipStatus = cust_circumstances_residency.getJSONArray("CitizenshipStatus").getJSONObject(0);
		
		//To store value of element in JSONObject
		String DOB = cust.getString("DOB");
		String DateOfResidency = cust_circumstances_residency.getString("DateOfResidency");
		String CountryCitizenCode = cust_circumstances_residency_citizenshipStatus.getString("CountryCitizenCode");
		
		//Display values in console
		System.out.println( "DOB: " + DOB );  
		System.out.println( "DateOfResidency: " + DateOfResidency ); 
		System.out.println( "CountryCitizenCode: " + CountryCitizenCode ); 
		
		//To modify element in JSONObject
		cust.put("DOB", "19980922");
		cust_circumstances_residency.put("DateOfResidency", "19980922");
		cust_circumstances_residency_citizenshipStatus.put("CountryCitizenCode", "US");
		
		//To store the new values
		DOB = cust.getString("DOB");
		DateOfResidency = cust_circumstances_residency.getString("DateOfResidency");
		CountryCitizenCode = cust_circumstances_residency_citizenshipStatus.getString("CountryCitizenCode");
		
		//Display new values in console
		System.out.println( "DOB: " + DOB );  
		System.out.println( "DateOfResidency: " + DateOfResidency ); 
		System.out.println( "CountryCitizenCode: " + CountryCitizenCode ); 
		
		//Create new json file by passing the filepath of json file being modified
		createJsonFile(json, configFileReader.getBodyScenario("1"));


		//System.setProperty("webdriver.chrome.driver", configFileReader.getDriverPath());
		//driver = new ChromeDriver();
		//driver.manage().window().maximize();
		//driver.manage().timeouts().implicitlyWait(configFileReader.getImplicitlyWait(), TimeUnit.SECONDS);
	}


	@SuppressWarnings("deprecation")
	@When("^user Executes Scenario1 Simple Assessment$")
	public void user_Executes_Scenario1_Simple_Assessment() throws IOException {

		//String baseURI  = "https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
		//String bodyScn = "D:\\DHS\\7JAN\\body_scenario1.json";
		//String userId   = "seri.charoensri@pega.com";
		//String passwd   = "rules";
		//File jsonFP		= new File(bodyScn);

		//RestAssured.baseURI = "https://api.iextrading.com/1.0";	// sample url


		String authCookie = (userId + ":" + passwd);
		String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
		String jsonBodyStr = generateStringFromResource(bodyScn);

		/*
    	RestAssured.given().
    	   			header("Authorization", "Basic "+ authCookieEncoded).	//->working:
	   				//auth().preemptive().basic(userId, passwd).  			//->working:
    	   			//auth().form(userId, passwd). 							//->java.lang.IllegalArgumentException: Failed to parse login page.
	   				contentType("application/json").
	   				body(jsonFP).
    	            //body(jsonBodyStr).
    	    when().
    	            post(baseURI).
    	    then().
    	            statusCode(200).
    	            body(Matchers.containsString("CLM")).
    	            log().all();

    	 //Matcher<String> matcher = Matchers.containsString("verified");

		 */


		response = RestAssured.given().
				header("Authorization", "Basic "+ authCookieEncoded).	//->working:
				//auth().preemptive().basic(userId, passwd).  			//->working:
				contentType("application/json").
				body(jsonFP).
				//body(jsonBodyStr).
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

		//=== STRING CASTING METHODS ====//
		// By using the ResponseBody.asString() method, we can convert the  body into the string representation.
		System.out.println("Response Body is: " + body.asString());
		Assert.assertEquals(body.asString().contains("\"StartDate\":\"2018-02-16\""), true);


		//=== JSON CASTING METHODS ==//
		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();
		ArrayList SSOR = jsonPathEvaluator.get("AssessmentResults.Customers.SSOR");
		String ssrVal0 = SSOR.get(0).toString();

		System.out.println("SSOR received from Response " + SSOR);
		Assert.assertEquals ("Correct SSOR received in the Response", "204794278", ssrVal0); // (msg, expected, actual)

	}



	@When("^entitlement Calculation Engine responds with default values$")
	public void entitlement_Calculation_Engine_responds_with_default_values() throws IOException {






	}





	public String generateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}

	////Use Json File as payload instead of converting to String as above case 
	//public void RestTest() throws Exception {
	//    File file = new File("/Users/bmishra/Code_Center/stash/experiments/src/main/resources/Search.json");
	//    String content = null;
	//
	//    RestAssured.given().body(file).with().contentType("application/json").then().expect().
	//            statusCode(200).
	//            body(Matchers.equalTo("true")).when().post("http://devsearch");
	//}
	////

	public JSONObject convertJsonFileToObject(String jsonPath) throws JSONException, IOException {
		String jsonBodyStr = generateStringFromResource(jsonPath);
		JSONObject test = new JSONObject( jsonBodyStr ); 
		return  test;
	}

	public void createJsonFile(JSONObject newJson, String filepath) {
		String filename, newFilename;
		File f = new File(filepath);
		filename = filepath.substring(filepath.lastIndexOf("\\")+1);
		int version = 1;
		while (f.exists())
		{
			newFilename = filename.substring(0, filename.length()-5) + "_" + version + ".json";
			f = new File(configFileReader.getFilesPath() + newFilename);
			version++;
		}

		try (FileWriter file = new FileWriter(f)) {

			file.write(newJson.toString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

		bodyScn = f.getAbsolutePath();  		
		jsonFP	 = new File(bodyScn);
	}
	

}
