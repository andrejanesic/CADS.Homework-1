Feature: IConfig.CFG4J

  Scenario: bad path and invalid properties
    Given properties file with syntax errors
    When passed properties path is not valid
    Then throw config exception

  Scenario: syntax error in properties
    Given properties file with syntax errors
    When passed properties path is valid
    Then throw config exception

  Scenario: bad properties
    Given properties file with bad settings but without syntax errors
    When passed properties path is valid
    Then throw config exception

  Scenario: bad path
    Given properties file without syntax errors
    When passed properties path is not valid
    Then throw config exception

  Scenario: good properties
    Given properties file without syntax errors
    When passed properties path is valid
    Then parsed properties match
