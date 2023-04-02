package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.DirectoryCrawlerException;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Controls {@link DirectoryCrawlerWorker} threads.
 */
@Singleton
public class DirectoryCrawler extends IDirectoryCrawler {

    @Getter
    private final Set<String> directoryPaths = new HashSet<>();
    @Getter
    private final Object directoryPathsLock = new Object();
    private final IJobQueue jobQueue;
    private final IConfig config;
    private final ICLOutput iclOutput;
    private final IExceptionHandler exceptionHandler;
    private DirectoryCrawlerWorker directoryCrawlerWorker;

    @Inject
    public DirectoryCrawler(
            IJobQueue jobQueue,
            IConfig config,
            ICLOutput iclOutput,
            IExceptionHandler exceptionHandler
    ) {
        this.jobQueue = jobQueue;
        this.config = config;
        this.iclOutput = iclOutput;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void crawl(Set<String> directoryPaths) {
        if (directoryPaths == null) {
            DirectoryCrawlerException e = new DirectoryCrawlerException(
                    "DirectoryCrawler::crawl directoryPaths must not be null"
            );
            if (exceptionHandler == null)
                throw new RuntimeComponentException(e);
            exceptionHandler.handle(e);
        }
        synchronized (directoryPathsLock) {
            this.directoryPaths.addAll(directoryPaths);
            directoryPathsLock.notifyAll();
        }
        if (directoryCrawlerWorker != null) {
            return;
        }
        directoryCrawlerWorker = new DirectoryCrawlerWorker(
                exceptionHandler,
                iclOutput,
                jobQueue,
                config.getConfig(),
                this.directoryPaths,
                this.directoryPathsLock
        );
        startNewThread(directoryCrawlerWorker);
    }

    @Override
    public void crawl(String directoryPath) {
        crawl(Collections.singleton(directoryPath));
    }

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {
        keepAlive();
    }

    @Override
    public void beforeEnd() {
        directoryPathsLock.notifyAll();
        super.beforeEnd();
    }
}
