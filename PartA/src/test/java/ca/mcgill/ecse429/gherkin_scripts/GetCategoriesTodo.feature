Feature: Get categories todo
    # As a user, I want to be able to get the todos associated with a category,so I can manage my todos better
  Scenario: Successfully get all todo items associated with a category
    Given a category exists with title "old title"
    Given a todo item exists with title "Test todo"
    Given the todo item with title "Test todo" is associated with the category
    When a request is sent to get the todos associated with the category
    Then the response should contain a 200 status code
    And the response body should contain a list of todo items

  Scenario: Getting all todos associated with a non-existent category
    Given a todo item exists with title "Test todo"
    When a request is sent to get the todos with title"Test todo" using a invalid category id "999"
    Then the response should contain a 404 status code

  Scenario: Getting empty todos associated with a category
    Given a category exists with title "old title"
    When a request is sent to get the todos associated with the category
    Then the response should contain a 200 status code
    And the response body should contain an empty list of todo items


