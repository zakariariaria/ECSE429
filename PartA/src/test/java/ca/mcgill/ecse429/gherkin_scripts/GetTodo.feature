#As a user, I want to view all my todo items, so I can manage my tasks effectively. 
Feature: Getting todo item

  Scenario: Successfully getting all todo items 
    When a request is sent to get all todo items
    Then the response should contain a 200 status code
    And the response body should contain a list of todo items

  Scenario: Successfully getting a specific todo item
    When a request is sent to get a todo item with id "1"
    Then the response should contain a 200 status code
    And the response body should contain a todo item with "scan paperwork" and "" and "false"

  Scenario: Getting a todo item with a non-existant id
    When a request is sent to get a todo item with id "999"
    Then the response should contain a 404 status code