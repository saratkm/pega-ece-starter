//package starter.stepdefinitions;
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.commons.net.util.Base64;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.openqa.selenium.WebDriver;
//
//import au.com.bytecode.opencsv.CSVReader;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import dataProviders.ConfigFileReader;
//import dataProviders.JsonManipulator;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import io.restassured.path.json.JsonPath;
//import io.restassured.response.Response;
//import io.restassured.response.ResponseBody;
//import junit.framework.Assert;
//import managers.Csv;
//import managers.CsvParser;
//
//public class Scenario3StepDefinitions {
//	@Given("^User initiatiates scenario \"([^\"]*)\"$")
//	public void user_initiatiates_scenario(String arg1) {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Payload data input IN_SCN(\\d+)_TC(\\d+)_Default(\\d+) expected OUTSCN(\\d+)_TC(\\d+)_Default(\\d+) status pass$")
//	public void payload_data_input_IN_SCN__TC__Default_expected_OUTSCN__TC__Default_status_pass(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Customer added an income that is more than the threshold having income bank credits$")
//	public void customer_added_an_income_that_is_more_than_the_threshold_having_income_bank_credits() {
//	    // Write code here that turns the phrase above into concrete actions
//		CsvParser entries = new CsvParser("D:\\DHS\\7JAN\\TestScenario- 3- JV.csv");
//		System.out.println(entries.getEntry("Income_5", "Cust_HdrAddrLine"));
//
//		JsonManipulator a = new JsonManipulator("D:\\DHS\\7JAN\\body_scenario3.json");
//		a.selectKey("Customer");
//		a.openArray("Circumstances");
//		a.selectKey("IncomeBank");
////				a.openArray("CitizenshipStatus");
//		System.out.println(a.getDataEntry("Credits"));
//		a.putDataEntry("Credits", entries.getEntry("Income_1", "Cust_Circ_IncomeBank_Credits"));
//		a.saveAs("D:\\DHS\\7JAN\\body_scenario3_2.json");
//		System.out.println(a.getCurrentKey());
//		a.backTrack(2);
//		System.out.println(a.getCurrentKey());
//	}
//
//	@When("^TC OneA Youth allowance is calculated based on income history$")
//	public void tc_OneA_Youth_allowance_is_calculated_based_on_income_history() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Then("^TC OneA Check if youth allowance calculation is correct$")
//	public void tc_OneA_Check_if_youth_allowance_calculation_is_correct() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Payload data input IN_SCN(\\d+)_TC(\\d+)_Valid(\\d+) expected OUTSCN(\\d+)_TC(\\d+)_Valid(\\d+) status fail$")
//	public void payload_data_input_IN_SCN__TC__Valid_expected_OUTSCN__TC__Valid_status_fail(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Payload data input IN_SCN(\\d+)_TC(\\d+)_Invalid(\\d+) expected OUTSCN(\\d+)_TC(\\d+)_Invalid(\\d+) status fail$")
//	public void payload_data_input_IN_SCN__TC__Invalid_expected_OUTSCN__TC__Invalid_status_fail(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Payload data input IN_SCN(\\d+)_TC(\\d+)_DummyTests expected OUTSCN(\\d+)_TC(\\d+)_DummyTests status pass$")
//	public void payload_data_input_IN_SCN__TC__DummyTests_expected_OUTSCN__TC__DummyTests_status_pass(int arg1, int arg2, int arg3, int arg4) {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Customer added an income that is less than the threshold having income bank credits$")
//	public void customer_added_an_income_that_is_less_than_the_threshold_having_income_bank_credits() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@When("^TC OneB Youth allowance is calculated based on income history$")
//	public void tc_OneB_Youth_allowance_is_calculated_based_on_income_history() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Then("^TC OneB Check if youth allowance calculation is correct$")
//	public void tc_OneB_Check_if_youth_allowance_calculation_is_correct() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Customer added two incomes having income bank credits$")
//	public void customer_added_two_incomes_having_income_bank_credits() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@When("^TC OneC Youth allowance is calculated based on income history$")
//	public void tc_OneC_Youth_allowance_is_calculated_based_on_income_history() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Then("^TC OneC Check if youth allowance calculation is correct$")
//	public void tc_OneC_Check_if_youth_allowance_calculation_is_correct() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Customer added two incomes having no income bank credits$")
//	public void customer_added_two_incomes_having_no_income_bank_credits() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@When("^TC OneD Youth allowance is calculated based on income history$")
//	public void tc_OneD_Youth_allowance_is_calculated_based_on_income_history() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Then("^TC OneD Check if youth allowance calculation is correct$")
//	public void tc_OneD_Check_if_youth_allowance_calculation_is_correct() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Given("^Customer moved to a new place to live$")
//	public void customer_moved_to_a_new_place_to_live() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@When("^TC TwoA Youth allowance is calculated based on change of dependency and income history$")
//	public void tc_TwoA_Youth_allowance_is_calculated_based_on_change_of_dependency_and_income_history() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//	@Then("^TC TwoA Check if youth allowance calculation is correct$")
//	public void tc_TwoA_Check_if_youth_allowance_calculation_is_correct() {
//	    // Write code here that turns the phrase above into concrete actions
//	    
//	}
//
//
//}
