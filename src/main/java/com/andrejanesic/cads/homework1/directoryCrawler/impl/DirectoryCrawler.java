package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;

import javax.inject.Inject;
import java.util.Set;

/**
 * Controls {@link DirectoryCrawlerWorker} threads.
 */
public class DirectoryCrawler extends IDirectoryCrawler {

    private final IConfig config;
    private DirectoryCrawlerWorker directoryCrawlerWorker;

    @Inject
    public DirectoryCrawler(IConfig config) {
        this.config = config;
    }

    @Override
    public void crawl(Set<String> directoryPaths) {
        if (directoryPaths == null || directoryPaths.size() == 0)
            // TODO handle better
            throw new RuntimeComponentException(
                    "DirectoryCrawler::crawl directoryPaths must not be null or empty"
            );
        if (directoryCrawlerWorker != null) {
            directoryCrawlerWorker.setDirectories(directoryPaths);
            return;
        }
        directoryCrawlerWorker = new DirectoryCrawlerWorker(config.getConfig(), directoryPaths);
        startNewThread(directoryCrawlerWorker);
    }

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {
        keepAlive();
    }

}
