package com.andrejanesic.cads.homework1.directoryCrawler;

import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.ConfigException;
import com.andrejanesic.cads.homework1.core.exceptions.DirectoryCrawlerException;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.directoryCrawler.impl.DirectoryCrawlerWorker;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.utils.MockConfigProperties;
import com.andrejanesic.cads.homework1.utils.MockDirectoryTree;
import com.andrejanesic.cads.homework1.utils.MockTextCorpus;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class ImplSteps {

    String dirPath;
    MockDirectoryTree mockDirectoryTree;

    @Inject
    @Mock
    IJobQueue jobQueueMock;

    @Inject
    @Mock
    IConfig configMock;
    AppConfiguration appConfigMock;
    DirectoryCrawlerWorker worker;

    @Before
    public void setUp() throws ConfigException {
        dirPath = "temp-" + System.currentTimeMillis();
        appConfigMock = new MockConfigProperties(
                "app.properties",
                true,
                new String[]{"__kw1__", "__kw2__", "__kw3__"},
                "prefix_",
                1000,
                1024,
                2,
                60_000,
                "\\s+"
        );
        MockitoAnnotations.openMocks(this);
        when(configMock.getConfig()).thenReturn(appConfigMock);
        try {
            doAnswer((invocation) -> {
                return null;
            }).when(jobQueueMock).enqueueJob(any(IJob.class));
        } catch (JobQueueException e) {
            throw new RuntimeException(e);
        }
        DirectoryCrawlerWorker.getIndexedDirs().clear();
    }

    @BeforeStep
    public void setUpStep() {
        DirectoryCrawlerWorker.getIndexedDirs().clear();
    }

    @After
    public void tearDown() {
        try {
            if (mockDirectoryTree != null) {
                mockDirectoryTree.remove();
            }
        } catch (IOException ignored) {
            ;
        }
    }

    @Given("empty target directory")
    public void empty_target_directory() throws IOException {
        dirPath = "temp-" + System.currentTimeMillis();
        mockDirectoryTree = new MockDirectoryTree(
                dirPath,
                appConfigMock.fileCorpusPrefix(),
                1,
                1,
                0,
                (int) (Math.random() * 100 + 1),
                new MockTextCorpus.Builder(
                        "",
                        "",
                        new String[]{},
                        0,
                        2048
                )
        );
        mockDirectoryTree.generate();
    }

    @When("directory path is invalid")
    public void directory_path_is_invalid() {
        dirPath = "invalid-path-" + dirPath;
    }

    @Then("throw directory crawler exception")
    public void throw_directory_crawler_exception() throws IOException {
        worker = new DirectoryCrawlerWorker(
                jobQueueMock,
                appConfigMock,
                new HashSet<>(List.of(dirPath))
        );
        assertThrows(DirectoryCrawlerException.class, worker::loop);
        if (mockDirectoryTree != null) {
            mockDirectoryTree.remove();
        }
    }

    @Given("target directory with subdirectories")
    public void target_directory_with_subdirectories() throws IOException {
        mockDirectoryTree = new MockDirectoryTree(
                dirPath,
                appConfigMock.fileCorpusPrefix(),
                2,
                (int) (Math.random() * 6) + 2,
                (int) (Math.random() * 15) + 1,
                (int) (Math.random() * 30) + 20,
                new MockTextCorpus.Builder(
                        "any",
                        " ",
                        new String[]{},
                        0,
                        2048
                )
        );
        mockDirectoryTree.generate();
    }

    @When("directory path is valid")
    public void directory_path_is_valid() {
        ;
    }

    @Then("no prefix directories indexed")
    public void no_prefix_directories_indexed() throws IOException {
        worker = new DirectoryCrawlerWorker(
                jobQueueMock,
                appConfigMock,
                new HashSet<>(List.of(dirPath))
        );
        assertDoesNotThrow(worker::loop);
        assertEquals(0, DirectoryCrawlerWorker.getIndexedDirs().values().size());
        if (mockDirectoryTree != null) {
            mockDirectoryTree.remove();
        }
    }

    @Then("all prefix directories indexed")
    public void all_prefix_directories_indexed() throws IOException {
        worker = new DirectoryCrawlerWorker(
                jobQueueMock,
                appConfigMock,
                new HashSet<>(List.of(dirPath))
        );
        assertDoesNotThrow(worker::loop);
        DirectoryCrawlerWorker.getIndexedDirs().forEach((absPath, lastMod) -> {
            assertTrue(mockDirectoryTree.getPrefixDirs().contains(absPath));
        });
        mockDirectoryTree.getPrefixDirs().forEach((dir) -> {
            assertTrue(DirectoryCrawlerWorker.getIndexedDirs().containsKey(dir));
        });
        if (mockDirectoryTree != null) {
            mockDirectoryTree.remove();
        }
    }
}
