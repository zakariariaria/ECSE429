#As a user, I want to update the details of a specific todo item, so I can correct or change the information as necessary.
Feature: Updating a todo item

  Scenario: Successfully updating all fields of a todo item 
  	Given a todo item exists with title "Test Update 1" 
    When a request is sent to update the fields of the todo item to title "Updated title 1" and description "Updated description 1" and doneStatus "true"
    Then the response should contain a 200 status code
    And the response body should contain the updated todo item with title "Updated title 1" and description "Updated description 1" and doneStatus "false" 

  Scenario: Successfully updating a specific field of a todo item
  	Given a todo item exists with title "Test Update 2"
    When a request is sent to update the title of the todo item to "Updated title 2"
    Then the response should contain a 200 status code
    And the response body should contain the updated todo item with title "Updated title 2" and description "" and doneStatus "false"

  Scenario: Updating a todo item with a non-existant id
    When a request is sent to update a todo item with id "999"
    Then the response should contain a 404 status code