Feature: Delete Todo Project relationship
    # As a user, I want to delete a todo's project relationship to be able to keep better track of a todo's status

    # Normal Flow
    Scenario Outline: Delete todo and projects relationship
        Given a todo with id "<todo_id>"
        Given a project with id "<project_id>"
        When I delete relationship between todo "<todo_id>" and project "<project_id>" with status "<status>"
        Then deleted relationship for todo "<todo_id>" and project does not exist
        Examples:
        | todo_id | project_id | status |
        | 1       | 1          | 200    |
        | 2       | 1          | 200    |

    # Alternate Flow (malformed request despite being a string)
    Scenario Outline: Delete todo and projects relationship with an incorrect ID
        Given a project with id "<project_id>"
        When I delete relationship between todo "<todo_id>" and project "<project_id>" with status "<status>"
        Examples:
        | todo_id   | project_id | status |
        | #-#       | 1          | 404    |

    # Error Flow with a project that does not exist
    Scenario Outline: Delete todo and projects relationship with non-existing project
        Given a project with id "<project_id>"
        When I delete relationship between todo "<todo_id>" and project "<project_id>" with status "<status>"
        Examples:
        | todo_id | project_id | status |
        | 3       | 1          | 400    |