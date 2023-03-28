package com.andrejanesic.cads.homework1.config.impl;

import com.andrejanesic.cads.homework1.Main;
import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.core.Core;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConfigLoaderTest {

    @Test
    public void givenCorrectConfigFile_whenCorrectPath_thenLoadConfig() throws IOException {
        // Set up test
        String configPath = "test.app.properties";
        String[] keywords = new String[]{"test1", "test2", "test3"};
        String fileCorpusPrefix = "prefix_";
        Integer directoryCrawlerSleepTime = 1000;
        Long fileScanningSizeLimit = 2000L;
        Integer hopCount = 3000;
        Integer urlRefreshTime = 4000;
        String content = "keywords=" + String.join(",", keywords) +
                "\nfileCorpusPrefix=" + fileCorpusPrefix +
                "\ndirectoryCrawlerSleepTime=" + directoryCrawlerSleepTime +
                "\nfileScanningSizeLimit=" + fileScanningSizeLimit +
                "\nhopCount=" + hopCount +
                "\nurlRefreshTime=" + urlRefreshTime;

        // Assert file exists and is correct
        Path p = Paths.get(configPath);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join("\n", Files.readAllLines(p)));

        // Mock
        Main.init();
        Core coreSpy = spy(Main.getCore());
        IArgs argsSpy = spy(coreSpy.getArgs());
        when(coreSpy.getArgs()).thenReturn(argsSpy);
        when(argsSpy.configSource()).thenReturn(configPath);
        try (MockedStatic<Main> mocked = mockStatic(Main.class)) {
            mocked.when(Main::getCore).thenReturn(coreSpy);
            assertEquals(configPath, Main.getCore().getArgs().configSource());

            // Execute
            ConfigLoader configLoader = new ConfigLoader();
            AppConfiguration appConfiguration = configLoader.load();

            // Test
            assertArrayEquals(keywords, appConfiguration.keywords());
            assertEquals(fileCorpusPrefix, appConfiguration.fileCorpusPrefix());
            assertEquals(directoryCrawlerSleepTime, appConfiguration.directoryCrawlerSleepTime());
            assertEquals(fileScanningSizeLimit, appConfiguration.fileScanningSizeLimit());
            assertEquals(hopCount, appConfiguration.hopCount());
            assertEquals(urlRefreshTime, appConfiguration.urlRefreshTime());
        }
    }

    @Test
    public void givenCorrectConfigFile_whenWrongPath_thenThrowException() throws IOException {
        // Set up test
        String configPath = "test.app.properties";
        String wrongPath = "test-wrong-path.properties";
        String[] keywords = new String[]{"test1", "test2", "test3"};
        String fileCorpusPrefix = "prefix_";
        Integer directoryCrawlerSleepTime = 1000;
        Long fileScanningSizeLimit = 2000L;
        Integer hopCount = 3000;
        Integer urlRefreshTime = 4000;
        String content = "keywords=" + String.join(",", keywords) +
                "\nfileCorpusPrefix=" + fileCorpusPrefix +
                "\ndirectoryCrawlerSleepTime=" + directoryCrawlerSleepTime +
                "\nfileScanningSizeLimit=" + fileScanningSizeLimit +
                "\nhopCount=" + hopCount +
                "\nurlRefreshTime=" + urlRefreshTime;

        // Assert file exists and is correct
        Path p = Paths.get(configPath);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join("\n", Files.readAllLines(p)));

        // Mock
        Main.init();
        Core coreSpy = spy(Main.getCore());
        IArgs argsSpy = spy(coreSpy.getArgs());
        when(coreSpy.getArgs()).thenReturn(argsSpy);
        when(argsSpy.configSource()).thenReturn(wrongPath);
        try (MockedStatic<Main> mocked = mockStatic(Main.class)) {
            mocked.when(Main::getCore).thenReturn(coreSpy);
            assertEquals(wrongPath, Main.getCore().getArgs().configSource());

            // Execute
            ConfigLoader configLoader = new ConfigLoader();
            assertThrows(IllegalStateException.class, configLoader::load);
        }
    }

    @Test
    public void givenBadConfigFile_whenCorrectPath_thenThrowException() throws IOException {
        // Set up test
        String configPath = "test.app.properties";
        String[] keywords = new String[]{"test1", "test2", "test3"};
        String fileCorpusPrefix = "prefix_";
        Integer directoryCrawlerSleepTime = 1000;
        Long fileScanningSizeLimit = 2000L;
        Integer hopCount = 3000;
        Integer urlRefreshTime = 4000;
        String content = "keywords=" + String.join(",", keywords) +
                "\nfileCffeorpusPrefix=" + fileCorpusPrefix +
                "\ndirectoryCrawlerSleepTime=" + directoryCrawlerSleepTime +
                "\nfileSadvvvcanningSizeLimit=" + fileScanningSizeLimit +
                "\nhopCount=" + hopCount +
                "\nurlRefreshcxxTime=" + urlRefreshTime;

        // Assert file exists and is correct
        Path p = Paths.get(configPath);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join("\n", Files.readAllLines(p)));

        // Mock
        Main.init();
        Core coreSpy = spy(Main.getCore());
        IArgs argsSpy = spy(coreSpy.getArgs());
        when(coreSpy.getArgs()).thenReturn(argsSpy);
        when(argsSpy.configSource()).thenReturn(configPath);
        try (MockedStatic<Main> mocked = mockStatic(Main.class)) {
            mocked.when(Main::getCore).thenReturn(coreSpy);
            assertEquals(configPath, Main.getCore().getArgs().configSource());

            // Execute
            ConfigLoader configLoader = new ConfigLoader();
            assertThrows(NoSuchElementException.class, configLoader::load);
        }
    }
}
