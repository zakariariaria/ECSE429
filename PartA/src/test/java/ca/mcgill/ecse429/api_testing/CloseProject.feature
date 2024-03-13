#As a user, I want to be able to close a project to not mix a closed project with the open ones.

  Scenario: Successful closure of an active project
    Given the project manager is on the project details page
    And the project is in an active state
    When the project manager chooses to close the project
    Then the project is closed successfully

  Scenario: Alternative scenario - Closing an already closed project
    Given the project manager is on the project details page
    And the project is already closed
    When the project manager tries to close the project
    Then an error message is displayed, indicating that the project is already closed

  Scenario: Unsuccessful closure of a project that does not exist
    Given the project manager tries to close a non-existing project
    Then an error message is displayed, indicating that the project does not exist