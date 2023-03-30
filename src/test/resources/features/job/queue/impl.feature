Feature: Job.Queue.Impl

  Scenario: jobs get inserted in given order
    Given unlimited job queue
    When multiple threads enqueue jobs in any order
    Then all jobs are enqueued