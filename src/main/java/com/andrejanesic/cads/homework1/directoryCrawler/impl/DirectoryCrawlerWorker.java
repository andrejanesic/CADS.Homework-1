package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.DirectoryCrawlerException;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
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
    @NonNull
    private final IJobQueue jobQueue;
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
    @Setter
    private Set<String> directories;

    public DirectoryCrawlerWorker(
            IJobQueue jobQueue,
            @NonNull AppConfiguration appConfiguration,
            Set<String> directories) {
        this.jobQueue = jobQueue;
        this.appConfiguration = appConfiguration;
        this.directories = directories;
    }

    /**
     * Crawls the given directory iteratively using BFS.
     */
    @Override
    public void loop() throws ComponentException {
        try {
            for (String p : directories) {
                File curr = new File(p);
                visited.clear();
                if (!curr.exists() || !curr.isDirectory()) {
                    throw new DirectoryCrawlerException("Path " + curr + " does not exist or is not a directory");
                }
                content.addFirst(curr);
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

                        long lastMod = attributes.lastAccessTime().toMillis();
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
                                        // TODO handle excep
                                        throw new RuntimeComponentException(e);
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
                                        // TODO handle excep
                                        throw new RuntimeComponentException(e);
                                    }
                                    return lastMod;
                                }
                        );
                    } catch (IOException ex) {
                        // TODO handle excep
                        throw new RuntimeComponentException(ex);
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
            throw new DirectoryCrawlerException(e.getMessage());
        }
    }
}
