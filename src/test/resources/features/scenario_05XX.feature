#Feature: Scenario 03 Tests
#
  #@scn5_TestCase1
  #Scenario Outline: Income goes up exceeding threshold with income bank credits
    #Given User initiatiates scenario "5"
    #And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
    #And  user has first income and Reported Second Income
    #When user reported new earning SARAT
    #Then recalculation occurs SARAT
#
    #Examples: 
      #| INPUT_CSV_KEY            | EXPECTED_CSV_KEY         | TEST_STATUS |
      #| userReportedSecondIncome| userReportedSecondIncome  | pass        |
 #     | IN_SCN05_TC01_Valid02    | OUTSCN05_TC01_Valid02    | fail        |
 #     | IN_SCN05_TC01_Invalid03  | OUTSCN05_TC01_Invalid03  | fail        |
 #     | IN_SCN05_TC01_DummyTests | OUTSCN05_TC01_DummyTests | pass        |
#
  #@scn5_def
  #Scenario: User reported new earnings
    #Given user has first income<"userReportedSecondIncome">
    #When user reported new earning
    #Then recalculation occurs
    