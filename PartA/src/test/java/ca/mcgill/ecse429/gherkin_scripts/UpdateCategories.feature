#As a user, I want to update a category with specifying an ID, so that I can associate todos with categories quickly.
Feature: Create a new category

  Scenario: Successfully updating title field of a specific category
    Given a category exists with title "old title"
    When a request is sent to update the field of the category to title "new title"
    Then the response should contain a 200 status code
    And the response body should contain a category with title "new title"

  Scenario: Successfully updating title and description field of a specific category
    Given a category exists with title "old title"
    When a request is sent to update the field of the category to title "new title" and description "new description"
    Then the response should contain a 200 status code
    And the response body should contain a category with title "new title" and description "new description"

  Scenario: Updating a category with a non-existant id
    When a request is sent to update the title of a category with id "999" to title "new title"
    Then the response should contain a 404 status code



