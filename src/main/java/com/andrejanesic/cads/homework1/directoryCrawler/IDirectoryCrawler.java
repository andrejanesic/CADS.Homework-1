package com.andrejanesic.cads.homework1.directoryCrawler;

import com.andrejanesic.cads.homework1.core.MultiThreadedComponent;

import java.util.Set;

/**
 * Directory crawler. Crawls the given directories for new text corpus subdirectories.
 */
public abstract class IDirectoryCrawler extends MultiThreadedComponent {

    /**
     * Crawls the directories on the given paths. If crawler was already
     * started before, the directories are added to the set of crawled
     * directories.
     *
     * @param directoryPaths Set of paths to directories to be crawled. Can
     *                       be relative or absolute.
     */
    public abstract void crawl(Set<String> directoryPaths);

    /**
     * Crawls the directory on the given path. If crawler was already started
     * before, this directory is added to the set of crawled directories.
     *
     * @param directoryPath Path to the directory. Can be relative or absolute.
     */
    public abstract void crawl(String directoryPath);

}
