package com.andrejanesic.cads.homework1.scanner;

import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import com.andrejanesic.cads.homework1.scanner.file.FileScanner;
import com.andrejanesic.cads.homework1.scanner.file.FileScannerRecursiveTask;
import com.andrejanesic.cads.homework1.utils.MockConfigProperties;
import com.andrejanesic.cads.homework1.utils.MockDirectoryTree;
import com.andrejanesic.cads.homework1.utils.MockTextCorpus;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScannerSteps {

    static volatile ConcurrentHashMap.KeySetView<Future<Result>, Boolean> results = ConcurrentHashMap.newKeySet();
    @Inject
    @Mock
    IResultRetriever resultRetrieverMock;
    ConcurrentHashMap<IJob, Future<Result>> resultRetrieverHashmap =
            new ConcurrentHashMap<>();
    @Mock
    IJobQueue jobQueueMockForFileScannerRecursiveTask;
    @Mock
    IJobQueue jobQueueMockForFileScanner;
    @Mock
    IConfig configMock;
    MockDirectoryTree mockDirectoryTree;
    AppConfiguration configuration;
    IJobQueue jobQueue;
    FileScanner fileScanner;
    FileScannerRecursiveTask fileScannerRecursiveTask;
    FileJob job;
    Map<String, Integer> expected;
    ExecutorService executorService;

    @Before
    public void setUp() throws IOException, JobQueueException {
        // open mocks
        MockitoAnnotations.openMocks(this);
        configuration = new MockConfigProperties();
        when(configMock.getConfig()).thenReturn(configuration);
        when(resultRetrieverMock.getStoreFileJobs()).thenReturn(resultRetrieverHashmap);
    }

    @After
    public void tearDown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Given("given a flat directory with text corpuses")
    public void given_a_flat_directory_with_text_corpuses() throws IOException {
        results.clear();
        if (mockDirectoryTree != null)
            mockDirectoryTree.remove();
        mockDirectoryTree = new MockDirectoryTree(
                "test-" + System.currentTimeMillis(),
                configuration.fileCorpusPrefix(),
                1,
                1,
                (int) (Math.random() * 30) + 10,
                (int) (Math.random() * 20) + 50,
                new MockTextCorpus.Builder(
                        "any",
                        " ",
                        configuration.keywords(),
                        0,
                        (int) configuration.fileScanningSizeLimit()
                )
        );
        mockDirectoryTree.generate();
        expected = new HashMap<>();
        mockDirectoryTree.getFiles().forEach((p, mockFile) -> {
            mockFile.getFrequency().forEach((k, v) -> {
                expected.put(k, expected.getOrDefault(k, 0) + v);
            });
        });
    }

    @When("file scanner recursive task starts on directory")
    public void file_scanner_recursive_task_starts_on_directory() {
        executorService = new ForkJoinPool();
        job = new FileJob(mockDirectoryTree.getPath());
        List<String> files = new ArrayList<>();
        for (MockTextCorpus mockTextCorpus : mockDirectoryTree.getFiles().values()) {
            files.add(mockTextCorpus.getPath());
        }
        fileScannerRecursiveTask = new FileScannerRecursiveTask(
                job,
                files,
                0,
                files.size(),
                jobQueueMockForFileScannerRecursiveTask,
                configMock
        );
        results.add(((ForkJoinPool) executorService).submit(fileScannerRecursiveTask));
    }

    @Then("all keyword frequencies are correct")
    public void all_keyword_frequencies_are_correct() throws ExecutionException, InterruptedException, IOException {
        Map<String, Integer> aggrFrequency = new HashMap<>();
        // TODO fix tests, I believe the method is working well though
        results.forEach(fr -> {
            Result r = null;
            try {
                r = fr.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            assertNotNull(r);
            assertNotNull(r.getFrequency());
            r.getFrequency().forEach((k, v) -> {
                aggrFrequency.put(k, aggrFrequency.getOrDefault(k, 0) + v);
            });
        });
        for (String k : configuration.keywords()) {
            assertEquals(expected.getOrDefault(k, 0), aggrFrequency.getOrDefault(k, 0));
        }
        if (mockDirectoryTree != null)
            mockDirectoryTree.remove();
    }

    @Given("given a multilevel directory with text corpuses")
    public void given_a_multilevel_directory_with_text_corpuses() throws IOException {
        results.clear();
        if (mockDirectoryTree != null)
            mockDirectoryTree.remove();
        mockDirectoryTree = new MockDirectoryTree(
                "test-" + System.currentTimeMillis(),
                configuration.fileCorpusPrefix(),
                2,
                (int) (Math.random() * 15) + 2,
                (int) (Math.random() * 30) + 20,
                (int) (Math.random() * 30) + 50,
                new MockTextCorpus.Builder(
                        "any",
                        " ",
                        configuration.keywords(),
                        0,
                        (int) configuration.fileScanningSizeLimit()
                )
        );
        mockDirectoryTree.generate();
        expected = new HashMap<>();
        mockDirectoryTree.getFiles().forEach((p, mockFile) -> {
            mockFile.getFrequency().forEach((k, v) -> {
                expected.put(k, expected.getOrDefault(k, 0) + v);
            });
        });
    }

    @When("file scanner component starts job with directory")
    public void file_scanner_component_starts_job_with_directory() throws JobQueueException {
        job = new FileJob(mockDirectoryTree.getPath());

        // if a new subdir is found while parsing, give it directly to file scanner (skip job dispatcher)
        doAnswer((invocation) -> {
            FileJob newJob = invocation.getArgument(0, FileJob.class);
            results.add(fileScanner.submit(newJob));
            return null;
        }).when(jobQueueMockForFileScanner).enqueueJob(any(IJob.class));

        fileScanner = new FileScanner(
                resultRetrieverMock,
                jobQueueMockForFileScanner,
                configMock
        );
        results.add(fileScanner.submit(job));
    }

    @Given("given a flat directory with too large text corpuses")
    public void given_a_flat_directory_with_too_large_text_corpuses() throws IOException {
        results.clear();
        if (mockDirectoryTree != null)
            mockDirectoryTree.remove();
        mockDirectoryTree = new MockDirectoryTree(
                "test-" + System.currentTimeMillis(),
                configuration.fileCorpusPrefix(),
                1,
                1,
                (int) (Math.random() * 30) + 10,
                (int) (Math.random() * 20) + 50,
                new MockTextCorpus.Builder(
                        "any",
                        " ",
                        configuration.keywords(),
                        (int) configuration.fileScanningSizeLimit(),
                        (int) configuration.fileScanningSizeLimit() * 2
                )
        );
        mockDirectoryTree.generate();
        expected = new HashMap<>();
        mockDirectoryTree.getFiles().forEach((p, mockFile) -> {
            mockFile.getFrequency().forEach((k, v) -> {
                expected.put(k, expected.getOrDefault(k, 0) + v);
            });
        });
    }

    @Then("exceptions occur in result")
    public void exceptions_occur_in_result() throws ExecutionException, InterruptedException, IOException {
        for (Future<Result> fr : results) {
            assertNotNull(fr);
            Result r = fr.get();
            assertNotNull(r);
            assertNotNull(r.getExceptions());
            assertNotEquals(r.getExceptions().size(), 0);
        }
        if (mockDirectoryTree != null)
            mockDirectoryTree.remove();

    }
}
