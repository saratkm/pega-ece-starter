package starter;

import cucumber.api.CucumberOptions;
import io.restassured.RestAssured;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.serenitybdd.rest.Ensure;

import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features"
)
public class CucumberTestSuite {
	
	static boolean beginFromHereCucumber =true;
	
    @Before
    public void setBaseUri() {
        RestAssured.baseURI = "https://infy-dhs-dt1.pegacloud.net";
    }

    @Test
    public void should_return_default_FTE_from_ECE() {
        when().get("/prweb/PRRestService/entitlement/v1/calculate/YAL-WIP1")
                .then().statusCode(200);

        Ensure.that("company name is returned", response -> response.body("companyName", equalTo("Apple Inc.")));
        Ensure.that("industry is returned", response -> response.body("industry", equalTo("Computer Hardware")));
    }
	
	
	
}
