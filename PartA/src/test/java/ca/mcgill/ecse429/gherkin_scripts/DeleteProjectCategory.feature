Feature: Delete project to category relationship
    # As a user, I want to delete a relationship (named category) for a project to be more organised.

    # Normal Flow
    Scenario Outline: Delete categories and projects relationship
        When I create relationship categories with title "<title>" for project with id "<project_id>"
        Then I delete project "<project_id>" relationship to category "<category_id>" relationship with status "<status>"
        Then there is no category for project "<project_id>"
        Examples:
        | category_id | title         | project_id | status |
        | 1           | assignment101 | 2          | 200    |

    # Alternate Flow (title with special character)
    Scenario Outline: Delete categories and projects relationship with special character title
        When I create relationship categories with title "<title>" for project with id "<project_id>"
        Then I delete project "<project_id>" relationship to category "<category_id>" relationship with status "<status>"
        Then there is no category for project "<project_id>"
        Examples:
        | category_id | title   | project_id | status |
        | 2           | #0!$%++ | 1          | 200    |

    # Error Flow
    Scenario Outline: Delete categories and projects relationship with category that does not exist
        Then I delete project "<project_id>" relationship to category "<category_id>" relationship with status "<status>"
        Examples:
        | category_id | project_id | status |
        | 3           |  1         | 404    |