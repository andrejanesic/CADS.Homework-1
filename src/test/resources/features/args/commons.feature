Feature: IArgs.Commons

  Scenario: no vars
    Given no arguments passed
    Then default arguments match

  Scenario: invalid vars
    Given invalid arguments passed
    Then throw args exception

  Scenario: good vars
    Given valid arguments passed
    Then parsed arguments match