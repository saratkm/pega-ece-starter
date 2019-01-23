#Feature: Scenario 02 tests
# A recalculation of a related entity on the ECE system should be triggered with status changes reports externally.
# parent FTB calculation for Shari when Amy applies for youth allowance.
#
  #
  #@scn2_Testcase2
  #Scenario Outline: Cancellation of FTB for Shari different values
    #Given User initiatiates scenario "2" 
    #And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
#	  And ShariIs currently Receiving FTB for Scenario Two Sarat  
#    When Amy applies for youth allowance with correct values
#    Then Confirm FTB is cancelled For Shari
#
    #Examples: 
      #| INPUT_CSV_KEY              	|EXPECTED_CSV_KEY         |TEST_STATUS|
      #| withEligibleValues					|eligibleValues					  | pass      |
#	    | IN_SCN01_TC01_Valid02     	|OUTSCN01_TC01_Valid02    | fail      |
#	    | IN_SCN01_TC01_Invalid03   	|OUTSCN01_TC01_Invalid03  | fail      |
#	    | IN_SCN01_TC01_DummyTests  	|OUTSCN01_TC01_DummyTests | pass      |
#
#
#
  #@scn2_Testcase1 @scn2_Default
  #Scenario: Cancellation of FTB for Shari 
    #Given Shari is <"currentlyReceivingFTB">  
    #When Amy applies for youth allowance with correct values
    #Then Confirm FTB is <"cancelledForShari">
    #
  #@scn2_Testcase2
  #Scenario: Continuation of FTB for Shari with Amy's incorrect Age
    #Given Shari is currently receiving FTB TIMS_CODE
    #When Amy applies for youth allowance with incorrect age
#    Then ConfirmFTB is continued for Shari in the response to age scenerio
    #
    