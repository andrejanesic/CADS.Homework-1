package com.andrejanesic.cads.homework1.directoryCrawler;

import com.andrejanesic.cads.homework1.core.IComponent;

/**
 * Directory crawler. Crawls the given directory for new text corpus sub-directories.
 */
public abstract class IDirectoryCrawler extends IComponent {

    /**
     * Starts crawling the directory on the given path.
     *
     * @param directoryPath Path to the directory. Can be relative or absolute.
     */
    public abstract void crawl(String directoryPath);

    /**
     * Gracefully shuts down the crawler.
     */
    public abstract void shutdown();
}
