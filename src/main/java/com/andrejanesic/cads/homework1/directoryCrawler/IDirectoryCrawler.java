package com.andrejanesic.cads.homework1.directoryCrawler;

import com.andrejanesic.cads.homework1.core.MultiThreadedComponent;

import java.util.Set;

/**
 * Directory crawler. Crawls the given directories for new text corpus subdirectories.
 */
public abstract class IDirectoryCrawler extends MultiThreadedComponent {

    /**
     * Starts crawling the directories on the given paths.
     *
     * @param directoryPaths Set of paths to directories to be crawled. Can be relative or absolute.
     */
    public abstract void crawl(Set<String> directoryPaths);

}
