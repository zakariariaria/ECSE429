Feature: Get projects from category
    # As a user, I want to see the project items related to a specific category so that I can keep track of a category and its projects.

    # Normal Flow
    Scenario Outline: See projects related to specific category
        Given a category with id "<category_id>"
        When I create project with title "<title>" for category with id "<category_id>" with status "<status>"
        Then verify project with title "<title>" exists under category "<category_id>"
        Examples:
        | category_id | title    | status |
        | 1           | theTitle |  201   |

    # Alternate Flow (title with special characters)
    Scenario Outline: See projects related to specific category with special title
        Given a category with id "<category_id>"
        When I create project with title "<title>" for category with id "<category_id>" with status "<status>"
        Then verify project with title "<title>" exists under category "<category_id>"
        Examples:
        | category_id | title    | status |
        | 1           | #!@$F&*$ |  201   |

    # Error Flow (no category with id)
    Scenario Outline: See projects related to specific category with non-existing category
        When I create project with title "<title>" for category with id "<category_id>" with status "<status>"
        Examples:
        | category_id | title   | status |
        | 0           | myTitle |  404   |