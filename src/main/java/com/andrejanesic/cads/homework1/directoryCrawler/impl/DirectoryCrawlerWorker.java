package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.DirectoryCrawlerException;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains all the logic behind crawling directories, creating jobs, etc. (the entire directory crawler functionality.)
 */
public class DirectoryCrawlerWorker extends LoopRunnable {

    /**
     * Hash map for all crawlers (only one) to mark files.
     */
    @Getter
    public static final ConcurrentHashMap<String, Long> indexedDirs = new ConcurrentHashMap<>();
    private final ICLOutput clOutput;
    @NonNull
    private final IJobQueue jobQueue;
    private final IExceptionHandler exceptionHandler;
    @NonNull
    private final AppConfiguration appConfiguration;
    /**
     * Deque of BFS nodes to visit.
     */
    private final Deque<File> content = new ArrayDeque<>();
    /**
     * BFS visited nodes.
     */
    private final Set<String> visited = new HashSet<>();
    /**
     * The directory that should be crawled.
     */
    @Getter
    private final Set<String> directories;
    @Getter
    private final Object directoriesLock;
    /**
     * Whenever a new directory is successfully added, we'll alert the user
     * that scanning has started.
     */
    private final Map<String, Boolean> alerted = new HashMap<>();

    /**
     * Default constructor.
     *
     * @param clOutput         command line output interface
     * @param jobQueue         job queue
     * @param appConfiguration app configuration
     * @param directories      set of directories to crawl
     * @param directoriesLock  for synchronized work on directories
     */
    public DirectoryCrawlerWorker(
            IExceptionHandler exceptionHandler,
            ICLOutput clOutput,
            IJobQueue jobQueue,
            @NonNull AppConfiguration appConfiguration,
            Set<String> directories,
            Object directoriesLock
    ) {
        this.exceptionHandler = exceptionHandler;
        this.clOutput = clOutput;
        this.jobQueue = jobQueue;
        this.appConfiguration = appConfiguration;
        this.directories = directories;
        this.directoriesLock = directoriesLock;
    }

    /**
     * @param jobQueue         job queue
     * @param appConfiguration app configuration
     * @param directories      set of directories to crawl
     * @deprecated use the new default constructor:
     * {@link #DirectoryCrawlerWorker(IExceptionHandler, ICLOutput, IJobQueue, AppConfiguration, Set, Object)}
     */
    public DirectoryCrawlerWorker(
            IJobQueue jobQueue,
            @NonNull AppConfiguration appConfiguration,
            Set<String> directories
    ) {
        this(null, null, jobQueue, appConfiguration, directories, null);
    }

    /**
     * Adds the initial directories to the search stack.
     *
     * @throws ComponentException
     */
    private void addInitial() throws ComponentException {
        Iterator<String> it = directories.iterator();
        while (it.hasNext()) {
            String p = it.next();
            File curr = new File(p);
            visited.clear();
            if (!curr.exists() || !curr.isDirectory()) {
                String err = "Path " + curr + " does not exist or is not " +
                        "a directory";
                DirectoryCrawlerException e =
                        new DirectoryCrawlerException(err);
                it.remove();
                if (exceptionHandler == null) {
                    throw e;
                }
                exceptionHandler.handle(e);
                continue;
            }
            content.addFirst(curr);
            if (alerted.containsKey(curr.getAbsolutePath())) continue;
            clOutput.info("Started crawling directory:\n" +
                    curr.getAbsolutePath());
            alerted.put(curr.getAbsolutePath(), true);
        }
    }

    /**
     * Crawls the given directory iteratively using BFS.
     */
    @Override
    public void loop() throws ComponentException {
        try {
            if (directoriesLock != null) {
                synchronized (directoriesLock) {
                    addInitial();
                }
            } else {
                addInitial();
            }

            if (directoriesLock != null) {
                synchronized (directoriesLock) {
                    while (directories.isEmpty())
                        directoriesLock.wait();
                }
            }

            // if initial path is not a directory, report error
            File curr;
            while (!content.isEmpty()) {
                curr = content.pollLast();

                if (!curr.exists() || !curr.isDirectory())
                    continue;

                // if directory, mark visited and check prefix
                visited.add(curr.getAbsolutePath());

                // if contains prefix, index it, otherwise add for recursion
                if (curr.getName().startsWith(appConfiguration.fileCorpusPrefix())) {
                    String absPath = curr.getAbsolutePath();
                    BasicFileAttributes attributes = null;

                    try {
                        attributes = Files.readAttributes(
                                Paths.get(absPath),
                                BasicFileAttributes.class
                        );

                        long lastMod = attributes.lastModifiedTime().toMillis();
                        indexedDirs.computeIfPresent(curr.getAbsolutePath(),
                                (key, val) -> {
                                    if (val == lastMod)
                                        return val;

                                    FileJob newJob = new FileJob(
                                            absPath
                                    );
                                    try {
                                        jobQueue.enqueueJob(newJob);
                                    } catch (JobQueueException e) {
                                        ComponentException ex =
                                                new DirectoryCrawlerException(e);
                                        if (exceptionHandler == null) {
                                            throw new RuntimeComponentException(ex);
                                        }
                                        exceptionHandler.handle(ex);
                                    }
                                    return lastMod;
                                });
                        indexedDirs.computeIfAbsent(
                                curr.getAbsolutePath(),
                                (key) -> {
                                    FileJob newJob = new FileJob(
                                            absPath
                                    );
                                    try {
                                        jobQueue.enqueueJob(newJob);
                                    } catch (JobQueueException e) {
                                        ComponentException ex =
                                                new DirectoryCrawlerException(e);
                                        if (exceptionHandler == null) {
                                            throw new RuntimeComponentException(ex);
                                        }
                                        exceptionHandler.handle(ex);
                                    }
                                    return lastMod;
                                }
                        );
                    } catch (IOException ex) {
                        DirectoryCrawlerException exc =
                                new DirectoryCrawlerException(ex);
                        if (exceptionHandler == null) {
                            throw exc;
                        }
                        exceptionHandler.handle(exc);
                    }
                }

                // traverse dir
                File[] files = curr.listFiles();
                if (files == null) continue;
                for (File nested : files) {
                    content.addFirst(nested);
                }
            }
        } catch (Exception e) {
            DirectoryCrawlerException ex =
                    new DirectoryCrawlerException(e);
            if (exceptionHandler == null) {
                throw ex;
            }
            exceptionHandler.handle(ex);
        }
    }
}
