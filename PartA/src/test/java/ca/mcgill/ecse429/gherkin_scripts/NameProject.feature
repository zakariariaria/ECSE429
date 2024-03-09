#As a user, I want to be able to modify the name of a Project by specifying a new name so users do not get mixed up between projects.

  Scenario: Successful modification of the name of a project
    Given the user is on the project details page
    And the project has a current name
    When the user modifies the name of the project
    And the user saves the changes
    Then the project's name is updated successfully

  Scenario: Alternative scenario - Modifying to a name that already exists
    Given the user is on the project details page
    And there is an existing project with the new name
    When the user modifies the name of the project
    And the user saves the changes
    Then an error message is displayed, indicating that the new project name already exists

  Scenario: Unsuccessful modification without specifying a new name
    Given the user is on the project details page
    When the user tries to save changes without specifying a new name
    Then an error message is displayed, indicating that the new project name is required
