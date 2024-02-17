#As a project manager, I want to assign a todo item to a specific project, so that tasks are organized according to their respective projects.
Feature: Assigning a todo to a project

  Scenario: Successfully assigning an existing todo to an existing project
    Given a todo item exists with title "Taskof"
    And a project exists
    When a request is sent to assign the todo item to the project
    Then the response should contain a 201 status code
    And the todo item with title "Taskof" should be succesfully associated with project

  Scenario: Assigning a todo item to a new project with additional fields
    Given a todo item exists with title "Taskof 2"
    When I send a request to create a new project with title "New Project" and active status "true"
    Then a new project should be created with the specified title and active status
    And the todo item with title "Taskof 2" should be succesfully associated with project
    
  Scenario: Assigning a non-existent todo
    When a request is sent to assign a todo item with id "999"
    Then the response should contain a 404 status code