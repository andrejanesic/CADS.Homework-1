package com.andrejanesic.cads.homework1.config;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;

public interface ApplicationConfig {

    /**
     * List of keywords to search for in the text corpuses. Comma-delimited.
     * Each keyword is counted only if it's standalone (not detected as part of
     * another keyword.)
     * @return Array of keywords.
     */
    String[] keywords();

    /**
     * The prefix that denotes that a directory contains text corpus files.
     * @return Prefix string.
     */
    String fileCorpusPrefix();

    /**
     * Indicates how long the {@link IDirectoryCrawler} should sleep.
     * @return Sleep time in ms.
     */
    int directoryCrawlerSleep();

    /**
     * Limits how large the text corpus may be.
     * @return File size in bytes.
     */
    int fileScanningSizeLimit();

    /**
     *
     * @return
     */
    int hopCount();
}
