	Feature: Scenario 03 Tests

  @scn5_TestCase1_A
  Scenario Outline: Income goes up exceeding threshold with income bank credits
     Given User initiatiates scenario "5" 
    And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
	  When user reported second earning
    Then recalculation occurs

    Examples: 
     | INPUT_CSV_KEY            | EXPECTED_CSV_KEY         | TEST_STATUS |
     | userReportedSecondIncome | deductionofbankcredits	 | pass        |
     

  @scn5_TestCase2_B
  Scenario Outline: Income goes up exceeding threshold and calculates new payment to be made by customer
     Given User initiatiates scenario "5" 
    And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
	  When customer will have debt scenario
    Then re-calculation occurs for payment to be done by user
      Examples: 
     | INPUT_CSV_KEY            | EXPECTED_CSV_KEY         | TEST_STATUS |
     | debtscenerio             | newpayment            	 | pass        |
    
    
    