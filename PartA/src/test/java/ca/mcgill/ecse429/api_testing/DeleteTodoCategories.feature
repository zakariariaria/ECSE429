#As a user, I want to delete the category associated with a todo, so that I can manage todos easily.
Feature: Delete todo category

  Scenario: Successfully deleting the relationship  between category and todo using the id
    Given a todo item exists with title "Test todo"
    Given a category exists with title "old title"
    Given the category with title "old title" is associated with the todo
    When a request is sent to delete the category with title "old title" associated with the todo using the id
    Then the response should contain a 200 status code


  Scenario: Successfully deleting the relationship  between category and todo using the id and category title
    Given a todo item exists with title "Test todo"
    Given a category exists with title "old title"
    Given the category with title "old title" is associated with the todo
    When a request is sent to delete the category with title "old title" associated with the todo using both the id and title
    Then the response should contain a 200 status code

  Scenario:Deleting the relationship  between category and todo without specifying the id
    Given a todo item exists with title "Test todo"
    Given a category exists with title "old title"
    Given the category with title "old title" is associated with the todo
    When a request is sent to delete the category with title "old title" associated with the todo without specifying the id
    Then the response should contain a 404 status code