package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;

/**
 * Controls {@link DirectoryCrawlerWorker} threads.
 */
public class DirectoryCrawler extends IDirectoryCrawler {

    private Thread singleThread;
    private DirectoryCrawlerWorker directoryCrawlerWorker;

    @Override
    public void crawl(String directoryPath) {
        if (singleThread != null) {
            directoryCrawlerWorker.setDirectory(directoryPath);
            return;
        }
        directoryCrawlerWorker = new DirectoryCrawlerWorker(directoryPath);
        singleThread = new Thread(directoryCrawlerWorker);
        singleThread.start();
    }

    @Override
    public void shutdown() {
        if (singleThread == null) return;
        directoryCrawlerWorker.terminate();
    }
}
