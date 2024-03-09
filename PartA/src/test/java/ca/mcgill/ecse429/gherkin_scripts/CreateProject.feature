#As a user, I want to be able to create a project by specifying a name for it so it is quick to instanciate a project.

  Scenario: Successful project creation with only the name specified
    Given the user is on the project creation page
    When the user specifies the name of the project
    And the user submits the form
    Then the project with the specified name is created successfully

  Scenario: Alternative scenario - Project name already exists
    Given the user is on the project creation page
    And there is an existing project with the same name
    When the user specifies the name of the project
    And the user submits the form
    Then an error message is displayed, indicating that the project name already exists

  Scenario: Unsuccessful project creation without specifying a name
    Given the user is on the project creation page
    When the user submits the form without specifying the project name
    Then an error message is displayed, indicating that the project name is required
