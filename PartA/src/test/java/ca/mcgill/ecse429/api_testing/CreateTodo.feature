#As a user, I want to create a new todo item without specifying an ID, so that I can quickly add tasks to my list.
Feature: Create a new todo item

  Scenario: Successfully creating a new todo item with all fields 
    When a request is sent to create a new todo item with title "title" and description "description"
    Then the response should contain a 201 status code
    And the response body should contain a todo item with title "title" and description "description" and doneStatus "false"

  Scenario: Creating a new todo item with only a title
    When a request is sent to create a new todo item with title "title"
    Then the response should contain a 201 status code
    And the response body should contain a todo item with title "title" and description "" and doneStatus "false"

  Scenario: Creating a new todo item with a missing title
    When a request is sent to create a new todo item with a missing title
    Then the response should contain a 400 status code