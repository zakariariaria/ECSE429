#As a user, I want to view all my categories, so I can manage my categories effectively.
Feature: Getting categories

  Scenario: Successfully getting all categories
    When a request is sent to get all categories
    Then the response should contain a 200 status code
    And the response body should contain a list of categories

  Scenario: Successfully getting a specific category
    Given a category exists with title "Work"
    When a request is sent to get a category with id "1"
    Then the response should contain a 200 status code

  Scenario: Getting a todo item with a non-existent id
    When a request is sent to get a category with id "999"
    Then the response should contain a 404 status code