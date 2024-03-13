#As a user, I want to delete a category with specifying an ID, so that I remove associate todos with categories quickly.
Feature: Delete a category

  Scenario: Successfully deleting a specific category with id
    Given a category exists with title "old title"
    When a request is sent to delete this category
    Then the response should contain a 200 status code

  Scenario: Deleting a category  without specifying id
    Given a category exists with title "old title"
    When a request is sent to delete this category without specifying its id
    Then the response should contain a 404 status code

  Scenario: Deleting a category with a non-existent id
    When a request is sent to delete a category with id "999"
    Then the response should contain a 404 status code