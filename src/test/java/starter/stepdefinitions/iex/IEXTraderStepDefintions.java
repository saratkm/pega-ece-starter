//package starter.stepdefinitions.iex;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import io.restassured.RestAssured;
//
//import org.hamcrest.Matchers;
//import org.apache.commons.net.util.Base64;
//
//
//public class IEXTraderStepDefintions {
//
//	@Given("^I am an active trader on IEX$")
//	public void i_am_an_active_trader() throws IOException {
//		String baseURI  = "https://infy-dhs-dt1.pegacloud.net/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1";
//		String bodyScn1 = "C:\\Users\\johnvincent.bisuna\\Desktop\\CD\\Request_Response\\Scenario1.json";
//		String userId   = "seri.charoensri@pega.com";
//		String passwd   = "rules";
//
//
//		//RestAssured.baseURI = "https://api.iextrading.com/1.0";
//		//String jsonBody = generateStringFromResource("D:\\DHS\\7JAN\\0-DHS-Export.postman_collection.json");
//
//
//		String authCookie = (userId + ":" + passwd);
//		String authCookieEncoded = new String(Base64.encodeBase64(authCookie.getBytes()));
//		String jsonBody = generateStringFromResource(bodyScn1);
//
//		RestAssured.given().
//		header("Authorization", "Basic "+ authCookieEncoded).      //->working:
//		//auth().preemptive().basic(userId, passwd).                       //->working:
//		//auth().form(userId, passwd).                                            //->java.lang.IllegalArgumentException: Failed to parse login page.
//		contentType("application/json").
//		body(jsonBody).
//		when().
//		post(baseURI).
//		then().
//		statusCode(200).
//		body(Matchers.containsString("CLM")).
//		log().all();
//
//		//Matcher<String> matcher = Matchers.containsString("verified");
//	}
//
//
//	public String generateStringFromResource(String path) throws IOException {
//		return new String(Files.readAllBytes(Paths.get(path)));
//	}
//
//}
