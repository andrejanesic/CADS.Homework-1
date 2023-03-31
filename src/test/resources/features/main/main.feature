Feature: Main

  Scenario: app startup
    Given main method is run with valid arguments
    Then every component is in the right life cycle state