Feature: Delete Todo Category Relationship
    # As a user, I want to delete a category to project relationship to be able to reorganize my todos.

    # Normal Flow
    Scenario Outline: Delete Categories and Projects Relationship
        Given a category with id "<category_id>"
        When I create project with title "<title>" for category with id "<category_id>" with status "<status_1>"
        Then I delete category "<category_id>" and project "<project_id>" relationship with status "<status_2>"
        Then there is no project for category "<category_id>"
        Examples:
            | category_id | title         | project_id | status_1 | status_2 |
            | 1           | assignment101 | 2          | 201      | 200      |
            | 1           | lmao444       | 2          | 201      | 200      |

    # Alternate Flow (Project with no title)
    Scenario Outline: Delete Categories and Projects Relationship wiht empty title
        Given a category with id "<category_id>"
        When I create project with title "<title>" for category with id "<category_id>" with status "<status_1>"
        Then I delete category "<category_id>" and project "<project_id>" relationship with status "<status_2>"
        Then there is no project for category "<category_id>"
        Examples:
            | category_id | title | project_id | status_1 | status_2 |
            | 1           |       | 2          | 201      | 200      |

    # Error Flow (Category and Project relationship does not exist)
    Scenario Outline: Delete Non-Existing Categories and Projects Relationship
    Then I delete category "<category_id>" and project "<project_id>" relationship with status "<status>"
        Examples:
            | category_id | project_id | status |
            | 69          | 33         | 404    |
            | 42          | 6          | 404    |