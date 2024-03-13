#As a user, I want to be able to retrieve Project Information by specifying its name to get all the information of a project at once.
Feature: Get a project 

  Scenario: Successful retrieval of all projects
   	When a request is sent to get all projects
  	Then the response should contain a 200 status code
    And the response body should contain a list of projects

  Scenario: Successfully getting a specific project 
  	When a request is sent to get a project with id "1"
    Then the response should contain a 200 status code
    And the response body should contain a project with "Office work" and ""

  Scenario: Getting a todo item with a non-existant id
    When a request is sent to get a todo item with id "999"
    Then the response should contain a 404 status code
    