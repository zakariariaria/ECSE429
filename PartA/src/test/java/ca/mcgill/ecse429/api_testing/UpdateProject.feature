#As a user, I want to update the details of a specific project item, so I can correct or change the information as necessary.
Feature: Updating a project item

  Scenario: Successfully updating all fields of a project item 
  	Given a project item exists with title "Test Update 1" 
    When a request is sent to update the fields of the project item to title "Updated title 1" and description "Updated description 1" and completed "true"
    Then the response should contain a 200 status code
    And the response body should contain the updated todo item with title "Updated title 1" and description "Updated description 1" and doneStatus "false" 

  Scenario: Successfully updating a specific field of a project item
  	Given a project exists with title "Test Update 2"
    When a request is sent to update the title of the project to "Updated title 2"
    Then the response should contain a 200 status code
    And the response body should contain the updated project item with title "Updated title 2" and description "" and completed "false"

  Scenario: Updating a project item with a non-existant id
    When a request is sent to update a project item with id "999"
    Then the response should contain a 404 status code