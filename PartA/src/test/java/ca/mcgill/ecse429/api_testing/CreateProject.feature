#As a user, I want to be able to create a project by specifying a name for it so it is quick to instanciate a project.
Feature: Create a project

  Scenario: Successful creating a project item with all fields
    When a request is sent to create a new project item with title "title" and description "description"
    Then the response should contain a 201 status code
    And the response body should contain a project item with title "title" and description "description"

  Scenario: Creating a new todo item with only a title
    When a request is sent to create a new project item with title "title"
    Then the response should contain a 201 status code
    And the response body should contain a project item with title "title" and description "" and completed "false"

  Scenario: Creating a new todo item with a missing title
    When a request is sent to create a new project item with a missing title
    Then the response should contain a 400 status code
    
    
    