Feature: Get projects from category
    # As a user, I want to see the project items related to a specific category so that I can keep track of a category and its projects.

    # Normal Flow
    Scenario: Get category related to specific project
        Given a category exists with title "Work"
        Given a project exist with title "Work"
        Given a association between the project and category exit
        When I send a request get the category of the project
        Then the response should contain a 200 status code



    # Alternate Flow (title with special characters)
    Scenario Outline: Get category related to specific project with special title
        Given a category exists with title "<title>"
        Given a project exist with title "Work"
        Given a association between the project and category exit
        When I send a request get the category of the project
        Then the response should contain a 200 status code

        Examples:
            | category_id | title    | status |
            | 1           | #!@$F&*$ |  201   |

    # Error Flow (no category with id)
    Scenario: Get non-existing category related to specific project
        Given a category exists with title "Work"
        Given a project exist with title "Work"
        When I send a request get the category of the project
        Then the response should contain a empty list of categories