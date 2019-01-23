Feature: Scenario 02 tests

  # A recalculation of a related entity on the ECE system should be triggered with status changes reports externally.
  # parent FTB calculation for Shari when Amy applies for youth allowance.
  @scn2_Testcase2
  Scenario Outline: Cancellation of FTB for Shari different values
    Given User initiatiates scenario "2"
    And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
    Given Shari is currently receiving FTB
    When Amy applies for youth allowance with correct values
    Then Confirm FTB is updated for Shari

    Examples: 
      | INPUT_CSV_KEY      | EXPECTED_CSV_KEY | TEST_STATUS |
      | EligibleValues     | eligibleValues   | pass        |
      | IncorrectAge       | age              | fail        |
      | IncorrectAgeResEdu | ageResEdu        | fail        |
