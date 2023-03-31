Feature: Scanner.(Web|File)

  Scenario: file scanner unit test
    Given given a flat directory with too large text corpuses
    When file scanner recursive task starts on directory
    Then exceptions occur in result

  Scenario: file scanner unit test
    Given given a flat directory with text corpuses
    When file scanner recursive task starts on directory
    Then all keyword frequencies are correct

#  Scenario: file scanner integration test
#    Given given a multilevel directory with text corpuses
#    When file scanner component starts job with directory
#    Then all keyword frequencies are correct