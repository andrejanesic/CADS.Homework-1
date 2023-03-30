package com.andrejanesic.cads.homework1.job.queue;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.job.queue.impl.JobQueue;
import com.andrejanesic.cads.homework1.utils.MockJob;
import com.andrejanesic.cads.homework1.utils.MockJobEnqueuerCallable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class ImplSteps {

    IJobQueue jobQueue;
    int threadCount;
    ExecutorService pool;
    Set<IJob> expectedSet;
    Set<IJob> receivedSet;
    ExecutorCompletionService<IJob> results;

    @Before
    public void setUp() {
        System.out.println("Setup");
        pool = Executors.newCachedThreadPool();
        results = new ExecutorCompletionService<>(pool);
    }

    @After
    public void tearDown() {
        pool.shutdown();
    }

    @Given("unlimited job queue")
    public void unlimited_job_queue() {
        jobQueue = new JobQueue();
    }

    @When("multiple threads enqueue jobs in any order")
    public void multiple_threads_enqueue_jobs_in_any_order() throws InterruptedException {
        threadCount = (int) (Math.random() * 20) + 1;
        expectedSet = new HashSet<>();
        receivedSet = new HashSet<>();
        for (int i = 0; i < threadCount; i++) {
            Object lock = new Object();
            IJob job = new MockJob(JobType.FILE);
            MockJobEnqueuerCallable mockJobEnqueuerCallable = new MockJobEnqueuerCallable(
                    job,
                    jobQueue,
                    lock
            );
            results.submit(mockJobEnqueuerCallable);
            expectedSet.add(job);

            assertDoesNotThrow(() -> {
                IJob received = jobQueue.dequeueJob();
                receivedSet.add(received);
            });
        }
    }

    @Then("all jobs are enqueued")
    public void all_jobs_are_enqueued() {
        assertEquals(expectedSet.size(), receivedSet.size());
        for (IJob t : expectedSet) {
            assertTrue(receivedSet.contains(t));
        }
    }
}
