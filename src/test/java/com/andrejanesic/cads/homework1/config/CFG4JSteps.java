package com.andrejanesic.cads.homework1.config;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.config.cfg4j.CFG4JLoader;
import com.andrejanesic.cads.homework1.core.exceptions.ConfigException;
import com.andrejanesic.cads.homework1.utils.MockConfigProperties;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CFG4JSteps {

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
    String pathValid = "test.app.properties";

    /**
     * Invalid path to valid config file.
     */
    String pathInvalid = "test.app.properties.xyz";

    /**
     * Valid config properties file.
     */
    MockConfigProperties config = null;

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

    /**
     * Config property: delimiter
     */
    String delimiter;

    @Before
    public void setUp() throws IOException {
        // open mocks
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() {
        try {
            if (config != null) {
                config.remove();
            }
        } catch (IOException ignored) {
            ;
        }
    }

    @Given("properties file with syntax errors")
    public void properties_file_with_syntax_errors() throws IOException {
        keywords = new String[]{"test1", "test2", "test3"};
        fileCorpusPrefix = "prefix_";
        directoryCrawlerSleepTime = 1000;
        fileScanningSizeLimit = 2000L;
        hopCount = 3000;
        urlRefreshTime = 4000;
        delimiter = "\\s+";
        config = new MockConfigProperties(
                pathValid,
                false,
                keywords,
                fileCorpusPrefix,
                directoryCrawlerSleepTime,
                fileScanningSizeLimit,
                hopCount,
                urlRefreshTime,
                delimiter
        );
        config.generate();
    }

    @When("passed properties path is not valid")
    public void passed_properties_path_is_not_valid() {
        when(iArgsMock.configSource()).thenReturn(pathInvalid);
    }

    @Then("throw config exception")
    public void throw_config_exception() {
        assertThrows(ConfigException.class, cfg4JLoaderMock::load);
    }

    @When("passed properties path is valid")
    public void passed_properties_path_is_valid() {
        when(iArgsMock.configSource()).thenReturn(pathValid);
    }

    @Given("properties file without syntax errors")
    public void properties_file_without_syntax_errors() throws IOException {
        // create a valid config
        keywords = new String[]{"test1", "test2", "test3"};
        fileCorpusPrefix = "prefix_";
        directoryCrawlerSleepTime = 1000;
        fileScanningSizeLimit = 2000L;
        hopCount = 3000;
        urlRefreshTime = 4000;
        delimiter = "\\s+";
        config = new MockConfigProperties(
                pathValid,
                true,
                keywords,
                fileCorpusPrefix,
                directoryCrawlerSleepTime,
                fileScanningSizeLimit,
                hopCount,
                urlRefreshTime,
                delimiter
        );
        config.generate();
    }

    @Given("properties file with bad settings but without syntax errors")
    public void properties_file_with_bad_settings_but_without_syntax_errors() throws IOException {
        // create a valid config
        keywords = new String[]{"test1", "test2", "test3"};
        fileCorpusPrefix = "prefix_";
        directoryCrawlerSleepTime = 1000;
        fileScanningSizeLimit = 2000L;
        hopCount = -1;
        urlRefreshTime = 4000;
        delimiter = "\\s+";
        config = new MockConfigProperties(
                pathValid,
                true,
                keywords,
                fileCorpusPrefix,
                directoryCrawlerSleepTime,
                fileScanningSizeLimit,
                hopCount,
                urlRefreshTime,
                delimiter
        );
        config.generate();
    }

    @Then("parsed properties match")
    public void parsed_properties_match() {
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
}
