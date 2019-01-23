Feature: Sample Calculation Scenarios 01

  @scn1
  Scenario Outline: Ineligible user applies for Youth Allowance
    Given User initiatiates scenario "1"
    And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
		When User applies for youth allowance
    Then Calculate eligibility, entitlement for user
    #    When Amy applies for youth allowance with incorrect values
    Examples: 
      | INPUT_CSV_KEY                     | EXPECTED_CSV_KEY                  | TEST_STATUS |
      | YAEligibilityInvalidAge           | YAEligibilityInvalidAge           | pass        |
      | YAEligibilityNotResident          | YAEligibilityNotResident          | pass        |
      | YAEligibilityInvalidAgeNotStudent | YAEligibilityInvalidAgeNotStudent | pass        |

  #	    | IN_SCN01_TC01_DummyTests  	|OUTSCN01_TC01_DummyTests 	| pass      |
  #@scn1_positive
  #Scenario: Calculate eligibility and entitlement for Amy
    
