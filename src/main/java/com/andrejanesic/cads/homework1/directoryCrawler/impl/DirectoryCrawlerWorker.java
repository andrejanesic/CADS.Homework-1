package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.DirectoryCrawlerException;
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
    private String directory;

    public DirectoryCrawlerWorker(@NonNull AppConfiguration appConfiguration, String directory) {
        this.appConfiguration = appConfiguration;
        this.directory = directory;
    }

    /**
     * Crawls the given directory iteratively using BFS.
     */
    @Override
    public void loop() throws ComponentException {
        try {
            File curr = new File(directory);
            visited.clear();
            if (!curr.exists() || !curr.isDirectory()) {
                throw new DirectoryCrawlerException("Path does not exist or is not a directory");
            }
            content.addFirst(curr);

            // if initial path is not a directory, report error
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

                        // TODO start new job here
                        indexedDirs.putIfAbsent(
                                curr.getAbsolutePath(),
                                attributes.lastModifiedTime().toMillis()
                        );
                    } catch (IOException ex) {
                        // TODO handle excep
                        throw new RuntimeException(ex);
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
