Feature: Scenario 03 Tests

  @scn3_TestCase1 @Historic @EligibleYA
  Scenario Outline: Change in income and income bank credits
    Given User initiatiates scenario "3"
    And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
    And Customer is already receiving youth allowance and income history is reported
    When Youth allowance is calculated based on income history
    Then Check if youth allowance calculation is correct based on income history

    Examples: 
      | INPUT_CSV_KEY                        | EXPECTED_CSV_KEY                     | TEST_STATUS |
      | IncomeGoesUpWithBankCredits          | IncomeGoesUpWithBankCredits          | pass        |
      | IncomeGoesDownWithBankCredits        | IncomeGoesDownWithBankCredits        | pass        |
      | IncomeGoesUpAndDownWithBankCredits   | IncomeGoesUpAndDownWithBankCredits   | pass        |
      | IncomeGoesUpAndDownWithNoBankCredits | IncomeGoesUpAndDownWithNoBankCredits | pass        |

  @scn3_TestCase2 @Historic @EligibleYA @RentalAssit
  Scenario Outline: Change of dependence status occurs
    Given User initiatiates scenario "3"
    And Payload data input <INPUT_CSV_KEY> expected <EXPECTED_CSV_KEY> status <TEST_STATUS>
    And Customer moved to a new place to live
    When Youth allowance is calculated based on change of dependency and income history
    Then Check if youth allowance calculation is correct based on change of dependency and income history

    Examples: 
      | INPUT_CSV_KEY                  | EXPECTED_CSV_KEY               | TEST_STATUS |
      | ChangeOfDependenceStatusOccurs | ChangeOfDependenceStatusOccurs | pass        |
