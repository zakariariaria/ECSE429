#As a user, I want to be able to retrieve Project Information by specifying its name to get all the information of a project at once.

  Scenario: Successful retrieval of project information by specifying its name
    Given the user is on the project information page
    When the user specifies the name of the project
    And the user requests project information
    Then the relevant project information is displayed

  Scenario: Alternative scenario - Project with specified name does not exist
    Given the user is on the project information page
    When the user specifies the name of a non-existing project
    And the user requests project information
    Then an error message is displayed, indicating that the project does not exist

  Scenario: Unsuccessful retrieval of project information without specifying a name
    Given the user is on the project information page
    When the user tries to request project information without specifying the name
    Then an error message is displayed, indicating that the project name is required
