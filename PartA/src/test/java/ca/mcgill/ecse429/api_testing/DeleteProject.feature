#As a user, I want to delete a project with specifying an ID, so that I remove associate todos with projects quickly.
Feature: Delete a project

    Scenario: Successfully deleting a specific category with id
        Given a project exists with title "projectTitle"
        When a request is sent to delete this project
        Then the response should contain a 200 status code

    Scenario: Deleting a category  without specifying id
        Given a category exists with title "projectTitle"
        When a request is sent to delete this project without specifying its id
        Then the response should contain a 404 status code

    Scenario: Deleting a category with a non-existent id
        When a request is sent to delete a project with id "999"
        Then the response should contain a 404 status code