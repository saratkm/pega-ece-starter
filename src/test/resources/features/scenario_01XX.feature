#Feature: Sample Calculation Scenarios 01
#
#
  #@scn2_Testcase2
  #Scenario Outline: Cancellation of FTB for Shari different values
    #Given User initiatiates scenario "1" 
    #And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
#	  And ShariIs currently Receiving FTBfor Scenario One
#    When Amy applies for youth allowance with correct values
#    Then Confirm FTB is cancelled For Shari
#
    #Examples: 
      #| INPUT_CSV_KEY              	|EXPECTED_CSV_KEY         	|TEST_STATUS|
      #| amyApplies									|eligibilityAndEntitlement	| pass      |
#	    | IN_SCN01_TC01_Valid02     	|OUTSCN01_TC01_Valid02    	| fail      |
#	    | IN_SCN01_TC01_Invalid03   	|OUTSCN01_TC01_Invalid03  	| fail      |
#	    | IN_SCN01_TC01_DummyTests  	|OUTSCN01_TC01_DummyTests 	| pass      |
#
#
#
#
  #@scn1_positive
  #Scenario: Calculate eligibility and entitlement for Amy
    #Given she lives at home with her parents, Lando and Shari
    #When <"amyApplies"> for youth allowance
    #Then Calculate <"eligibilityAndEntitlement"> for Amy
    