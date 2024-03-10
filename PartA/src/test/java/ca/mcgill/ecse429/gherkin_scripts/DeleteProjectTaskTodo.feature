Feature: Delete task relationship between project and todo
    # As a user, I want to delete a relationship between a project and todo (named tasks), to better represent the relationships of todos.

    # Normal Flow
    Scenario Outline: Delete task relationship between project and todo
        Given a todo with id "<todo_id>"
        Given a project with id "<project_id>"
        When I delete relationship between project "<project_id>" and todo "<todo_id>" with status "<status>"
        Then deleted relationship for project "<project_id>" and todo does not exist
        Examples:
        | todo_id | project_id | status |
        | 1       | 1          |  200   |

    # Alternate flow (Todo exists but no relationship)
    Scenario Outline: Delete task relationship between project and todo with existing todo but no relationship
        When I create the todo task "<new_todo_task>"
        Given a project with id "<project_id>"
        When I delete relationship between project "<project_id>" and todo "<todo_id>" with status "<status>"

        Examples:
        | new_todo_task | todo_id | project_id | status |
        | helloworld    |  3      | 1          |  404   |

    # Error Flow
    Scenario Outline: Delete task relationship between project and todo with invalid ID
        When I delete relationship between project "<project_id>" and todo "<todo_id>" with status "<status>"
        Examples:
        | todo_id | project_id | status |
        |  429    | 1          |  404   |