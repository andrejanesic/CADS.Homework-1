Feature: DirectoryCrawler.Impl

  Scenario: invalid path
    Given empty target directory
    When directory path is invalid
    Then throw directory crawler exception

  Scenario: invalid path
    Given target directory with subdirectories
    When directory path is invalid
    Then throw directory crawler exception

  Scenario: valid path
    Given empty target directory
    When directory path is valid
    Then no prefix directories indexed

  Scenario: valid path
    Given target directory with subdirectories
    When directory path is valid
    Then all prefix directories indexed