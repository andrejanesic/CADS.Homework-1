package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.config.AppConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Creates a properties file for the application with the specified parameters.
 */
@AllArgsConstructor
@Data
public class MockConfigProperties implements AppConfiguration {

    /**
     * Path to the config file.
     */
    @NonNull
    private final String path;

    /**
     * Whether the configuration file should be valid or have syntax errors (random).
     */
    @NonNull
    private final boolean valid;

    @NonNull
    private final String[] keywords;
    @NonNull
    private final String fileCorpusPrefix;
    @NonNull
    private final int directoryCrawlerSleepTime;
    @NonNull
    private final long fileScanningSizeLimit;
    @NonNull
    private final int hopCount;
    @NonNull
    private final int urlRefreshTime;

    @Override
    public String[] keywords() {
        return keywords;
    }

    @Override
    public String fileCorpusPrefix() {
        return fileCorpusPrefix;
    }

    @Override
    public int directoryCrawlerSleepTime() {
        return directoryCrawlerSleepTime;
    }

    @Override
    public long fileScanningSizeLimit() {
        return fileScanningSizeLimit;
    }

    @Override
    public int hopCount() {
        return hopCount;
    }

    @Override
    public int urlRefreshTime() {
        return urlRefreshTime;
    }

    /**
     * Writes the mock config file.
     */
    public void generate() throws IOException {
        String content = "keywords=" + String.join(",", keywords) +
                "\nfileCorpusPrefix=" + fileCorpusPrefix +
                (valid ? "" : "kkdjf\n") +
                "\ndirectoryCrawlerSleepTime=" + directoryCrawlerSleepTime +
                "\nfileScanni" +
                (valid ? "" : "\n") +
                "ngSizeLimit=" + fileScanningSizeLimit +
                "\nhopCount=" + hopCount +
                (valid ? "" : "hopCount:") +
                "\nurlRefreshTime=" + urlRefreshTime;

        // assert file exists and is correct
        Path p = Paths.get(path);
        Files.write(p, content.getBytes());
        assertEquals(content, String.join("\n", Files.readAllLines(p)));
    }

    /**
     * Deletes the mock config file.
     */
    public void remove() throws IOException {
        Files.delete(Paths.get(path));
    }
}
