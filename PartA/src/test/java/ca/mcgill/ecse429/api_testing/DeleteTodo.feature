#As a user, I want to delete a todo with specifying an ID, so that I remove it.
Feature: Delete a todo

  Scenario: Successfully deleting a specific todo with id
    Given a todo exists with title "old title"
    When a request is sent to delete this category
    Then the response should contain a 200 status code

  Scenario: Deleting a todo without specifying id
    Given a todo exists with title "old title"
    When a request is sent to delete this category without specifying its id
    Then the response should contain a 404 status code

  Scenario: Deleting a todo with a non-existent id
    When a request is sent to delete a category with id "999"
    Then the response should contain a 404 status code