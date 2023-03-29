package com.andrejanesic.cads.homework1.config.cfg4j;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.core.exceptions.ConfigException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CFG4JLoaderTest {

    /**
     * Mock {@link IArgs} object. "name" parameter must be the same as the internal variable name in the mocked class.
     */
    @Inject
    @Mock(name = "args")
    IArgs iArgsMock;

    /**
     * Mocked class.
     */
    @Inject
    @InjectMocks
    CFG4JLoader cfg4JLoaderMock;

    /**
     * Valid path to valid config file.
     */
    String configValidPathValid = "test.app.properties";

    /**
     * Invalid path to valid config file.
     */
    String configValidPathInvalid = "test.app.properties.xyz";

    /**
     * Valid path to invalid config file.
     */
    String configInvalidPathValid = "test-invalid.app.properties";

    /**
     * Config property: keywords
     */
    String[] keywords;

    /**
     * Config property: fileCorpusPrefix
     */
    String fileCorpusPrefix;

    /**
     * Config property: directoryCrawlerSleepTime
     */
    int directoryCrawlerSleepTime;

    /**
     * Config property: fileScanningSizeLimit
     */
    long fileScanningSizeLimit;

    /**
     * Config property: hopCount
     */
    int hopCount;

    /**
     * Config property: urlRefreshTime
     */
    int urlRefreshTime;

    @BeforeEach
    void beforeEach() throws IOException {
        // open mocks
        MockitoAnnotations.openMocks(this);

        // create a valid config
        keywords = new String[]{"test1", "test2", "test3"};
        fileCorpusPrefix = "prefix_";
        directoryCrawlerSleepTime = 1000;
        fileScanningSizeLimit = 2000L;
        hopCount = 3000;
        urlRefreshTime = 4000;
        String content = "keywords=" + String.join(",", keywords) +
                "\nfileCorpusPrefix=" + fileCorpusPrefix +
                "\ndirectoryCrawlerSleepTime=" + directoryCrawlerSleepTime +
                "\nfileScanningSizeLimit=" + fileScanningSizeLimit +
                "\nhopCount=" + hopCount +
                "\nurlRefreshTime=" + urlRefreshTime;

        // assert file exists and is correct
        Path p = Paths.get(configValidPathValid);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join("\n", Files.readAllLines(p)));

        // create an invalid config
        keywords = new String[]{"test1", "test2", "test3"};
        fileCorpusPrefix = "prefix_";
        directoryCrawlerSleepTime = 1000;
        fileScanningSizeLimit = 2000L;
        hopCount = 3000;
        urlRefreshTime = 4000;
        content = "keywords=" + String.join(",", keywords) +
                "\nfileCffeorpusPrefix=" + fileCorpusPrefix +
                "\ndirectoryCrawlerSleepTime=" + directoryCrawlerSleepTime +
                "\nfileSadvvvcanningSizeLimit=" + fileScanningSizeLimit +
                "\nhopCount=" + hopCount +
                "\nurlRefreshcxxTime=" + urlRefreshTime;

        // assert file exists and is correct
        p = Paths.get(configInvalidPathValid);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join("\n", Files.readAllLines(p)));
    }

    @AfterEach
    void afterEach() {
        new File(configValidPathValid).delete();
        new File(configInvalidPathValid).delete();
    }

    @Test
    public void givenCorrectConfigFile_whenCorrectPath_thenLoadConfig() throws IOException {
        // Set up test

        // Mock
        when(iArgsMock.configSource()).thenReturn(configValidPathValid);

        // Execute
        AtomicReference<AppConfiguration> appConfiguration = new AtomicReference<>();
        assertDoesNotThrow(() -> appConfiguration.set(cfg4JLoaderMock.load()));

        // Test
        assertArrayEquals(keywords, appConfiguration.get().keywords());
        assertEquals(fileCorpusPrefix, appConfiguration.get().fileCorpusPrefix());
        assertEquals(directoryCrawlerSleepTime, appConfiguration.get().directoryCrawlerSleepTime());
        assertEquals(fileScanningSizeLimit, appConfiguration.get().fileScanningSizeLimit());
        assertEquals(hopCount, appConfiguration.get().hopCount());
        assertEquals(urlRefreshTime, appConfiguration.get().urlRefreshTime());
    }

    @Test
    public void givenCorrectConfigFile_whenWrongPath_thenThrowException() {
        // Mock
        when(iArgsMock.configSource()).thenReturn(configValidPathInvalid);

        // Execute
        assertThrows(ConfigException.class, cfg4JLoaderMock::load);
    }

    @Test
    public void givenBadConfigFile_whenCorrectPath_thenThrowException() {
        // Mock
        when(iArgsMock.configSource()).thenReturn(configInvalidPathValid);

        // Execute
        assertThrows(ConfigException.class, cfg4JLoaderMock::load);
    }
}
