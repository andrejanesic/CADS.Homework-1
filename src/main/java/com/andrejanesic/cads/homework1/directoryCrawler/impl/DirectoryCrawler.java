package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;

import javax.inject.Inject;
import java.util.Set;

/**
 * Controls {@link DirectoryCrawlerWorker} threads.
 */
public class DirectoryCrawler extends IDirectoryCrawler {

    private Thread singleThread;
    private DirectoryCrawlerWorker directoryCrawlerWorker;
    private IConfig config;

    @Inject
    public DirectoryCrawler(IConfig config) {
        this.config = config;
    }

    @Override
    public void crawl(Set<String> directoryPaths) {
        if (singleThread != null) {
            directoryCrawlerWorker.setDirectories(directoryPaths);
            return;
        }
        directoryCrawlerWorker = new DirectoryCrawlerWorker(config.getConfig(), directoryPaths);
        singleThread = new Thread(directoryCrawlerWorker);
        singleThread.start();
    }

    @Override
    public void shutdown() {
        if (singleThread == null) return;
        directoryCrawlerWorker.stop();
    }
}
