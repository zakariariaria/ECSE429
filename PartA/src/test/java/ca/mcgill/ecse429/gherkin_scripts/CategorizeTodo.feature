#As a user, I want to categorize my todos, so I can filter them based on different categories
Feature: Categorize Todos

  Scenario: Creating a new category and associating it with a todo item
    Given a todo item exists with title "Todo Categories 1"
    When I send a request to create a new category with title "Home"
    Then the category "Home" should be successfully associated with the todo item
    
	Scenario: Associating an existing category with a todo item
      Given a category exists with title "Work"
    Given a todo item exists with title "Todo Categories 2"
    When I send a request to create a associate the category with the todo
    Then the category "Work" should be successfully associated with the todo item

  Scenario: Attempting to create a category without a title
    Given a todo item exists with title "Todo Categories 2"
    When I send a request to create a new category without a title
    Then the response should contain a 400 status code

 