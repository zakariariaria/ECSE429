Feature: Delete Todo Category Relationship
    # As a user, I want to delete a category to project relationship to be able to reorganize my todos.

    # Normal Flow
    Scenario: Delete Categories and Projects Relationship
        Given a category exists with title "categoryTitle"
        Given a project exists with title "projectTitle"
        When I send a request to create an association between the category to the project
        When I delete category "categoryTitle" and project "projectTitle" relationship
        Then the response should contain a 200 status code

    # Alternate Flow (Project with special title)
    Scenario: Delete Categories and Projects Relationship with special title
        Given a category exists with title "categoryTitle"
        Given a project exists with title "project#!Title"
        When I send a request to create an association between the category to the project
        When I delete category "categoryTitle" and project "project#!Title" relationship
        Then the response should contain a 200 status code

    # Error Flow (Category and Project relationship does not exist)
    Scenario: Delete Non-Existing Categories and Projects Relationship
        Given a category exists with title "categoryTitle"
        Given a project exists with title "projectTitle"
        When I delete category "categoryTitle" and project "projectTitle" relationship
        Then the response should contain a 404 status code