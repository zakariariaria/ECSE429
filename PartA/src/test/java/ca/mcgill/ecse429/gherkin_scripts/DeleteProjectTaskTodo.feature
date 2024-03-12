Feature: Delete task relationship between project and todo
    # As a user, I want to delete a relationship between a project and todo (named tasks), to better represent the relationships of todos.

    # Normal Flow
    Scenario: Delete task relationship between project and todo
        Given a todo item exists with title "todoTitle"
        Given a project exists with title "projectTitle"
        When I send a request to create an association between the project to task
        When I delete project "projectTitle" and todo "todoTitle" relationship
        Then the response should contain a 200 status code

    # Alternate flow (title with special character)
    Scenario: Delete task relationship between project and todo with existing todo but no relationship
        Given a todo item exists with title "todo#!Title"
        Given a project exists with title "projectTitle"
        When I send a request to create an association between the project to todo
        When I delete project "projectTitle" and todo "todo#!Title" relationship
        Then the response should contain a 200 status code

    # Error Flow
    Scenario: Delete task relationship between project and todo with invalid ID
        Given a todo item exists with title "todo#!Title"
        Given a project exists with title "projectTitle"
        When I delete project "projectTitle" and todo "todo#!Title" relationship
        Then the response should contain a 404 status code