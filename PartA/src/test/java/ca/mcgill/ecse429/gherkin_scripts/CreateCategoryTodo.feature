Feature: Add Category Todo Relationship
    # As a user, I want to be able to create a category-todo relationship, so that I can better organise my todos.

    # Normal Flow
    Scenario Outline:  Create a relationship between an existing category and an existing todo
        Given an existing category with title "<existing_category>"
        And an existing todo with title "<existing_todo>"
        When the user creates a relationship between "<existing_category>" and "<existing_todo>"
        Then the expected category status received from the system is "<status>"
        And "<existing_todo>" will be part of "<existing_category>"
        Examples:
        | existing_category | existing_todo | status |
        | painting          | sunset        | 201    |

    # Alternate Flow
    Scenario Outline: Create a relationship with a category when creating a todo
        Given an existing todo with title "<existing_todo>"
        When the user creates a new category "<new_category>" with todo "<existing_todo>"
        Then the expected category status received from the system is "<status>"
        And "<existing_todo>" will be part of "<new_category>"
        Examples:
        | new_category | existing_todo | statusCode |
        | drawing      | portrait      | 201        |

    # Error Flow (no category with id)
    @scenarioOutline
    Scenario Outline: Create a relationship between a todo and a category that doesn't exist
        When the user creates a relationship between a new todo "<new_todo>" with a category "<invalid_category>" that doesn't exist
        Then the expected category status received from the system is "<status>"
        Examples:
        | invalid_category | new_todo | statusCode |
        | 21lol            | sunrise  | 400        |