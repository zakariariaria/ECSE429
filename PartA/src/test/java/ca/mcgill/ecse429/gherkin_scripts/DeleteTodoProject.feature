Feature: Delete Todo Project relationship
    # As a user, I want to delete a todo's project relationship to be able to keep better track of a todo's status

    # Normal Flow
    Scenario: Successfully Delete todo and projects relationship
        Given a project exist with title "Work"
        Given a todo item exists with title "Title"
        Given a association between the project and todo item exist
        When a request is made to delete the todo and project relationship
        Then the response should contain a 200 status code


    Scenario: Delete  projects diretly so that the association is removed
        Given a project exist with title "Work"
        Given a todo item exists with title "Title"
        Given a association between the project and todo item exist
        When a request is made to delete the project
        When a request is sent to get the project from the category
        Then the response body should not contain the project with title "Work"

    Scenario: Delete  non-existent todo and projects relationship
        Given a project exist with title "Work"
        Given a todo item exists with title "Title"
        When a request is made to delete the todo and project relationship
        Then the response should contain a 404 status code