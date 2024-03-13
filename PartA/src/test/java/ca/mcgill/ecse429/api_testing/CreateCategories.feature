#As a user, I want to create a new category without specifying an ID, so that I can associate todos with categories quickly.
Feature: Create a new category

  Scenario: Successfully creating a new category with title
    When a request is sent to create a new category with title "title"
    Then the response should contain a 201 status code
    And the response body should contain a category with title "title"


  Scenario: Successfully creating a new category with title and description
    When a request is sent to create a new category with title "title" and description "description"
    Then the response should contain a 201 status code
    And the response body should contain a category with title "title" and description "description"

  Scenario: Creating a new category with a missing title
    When a request is sent to create a new category  with a missing title
    Then the response should contain a 400 status code