Feature: User can book a ticket


  Scenario: User can book a ticket for event
    Given ser with name "John" and email "john@example.com" and account balance $500
    And an event titled "Concert" with a ticket price of $50
    When the user books a ticket for the event
    Then the booking should be successful

