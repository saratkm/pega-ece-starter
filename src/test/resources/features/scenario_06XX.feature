#Feature: Calculation of YA Scenarios 06
  #
  #@scn2_Testcase2
  #Scenario Outline: Cancellation of FTB for Shari different values
    #Given User initiatiates scenario "6" 
    #And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
#	  And Shari is currently Receiving FTB scenario six  
#    When Amy applies for youth allowance with correct values
#    Then Confirm FTB is cancelled For Shari
#
    #Examples: 
      #| INPUT_CSV_KEY              	|EXPECTED_CSV_KEY         |TEST_STATUS|
      #| Test01ADependantBDependant	|youthallowanceamount			| pass      |
#	    | IN_SCN01_TC01_Valid02     	|OUTSCN01_TC01_Valid02    | fail      |
#	    | IN_SCN01_TC01_Invalid03   	|OUTSCN01_TC01_Invalid03  | fail      |
#	    | IN_SCN01_TC01_DummyTests  	|OUTSCN01_TC01_DummyTests | pass      |
#
#
#@scn6_positive
#Scenario: Sibling Application
#	Given 	User applies for youth allowance <Test01ADependantBDependant>
#	When 		Sibling is already receiving youth allowance and is dependant
#	Then 		Verify the <youthallowanceamount> received by both the siblings
