Feature: Delete project to category relationship
    # As a user, I want to delete a relationship (named category) for a project to be more organised.

    # Normal Flow
    Scenario: Delete categories and projects relationship
        Given a category exists with title "categoryTitle"
        Given a project exists with title "projectTitle"
        When I send a request to create an association between the project to the category
        When I delete project "projectTitle" and category "categoryTitle" relationship
        Then the response should contain a 200 status code

    # Alternate Flow (title with special character)
    Scenario: Delete categories and projects relationship with special character title
        Given a category exists with title "category#!Title"
        Given a project exists with title "projectTitle"
        When I send a request to create an association between the project to the category
        When I delete project "projectTitle" and category "category#1Title" relationship
        Then the response should contain a 200 status code

    # Error Flow
    Scenario: Delete categories and projects relationship with relationship that does not exist
        Given a category exists with title "categoryTitle"
        Given a project exists with title "projectTitle"
        When I delete project "projectTitle" and category "categoryTitle" relationship
        Then the response should contain a 404 status code