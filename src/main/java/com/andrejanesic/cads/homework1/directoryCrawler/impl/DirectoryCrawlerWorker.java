package com.andrejanesic.cads.homework1.directoryCrawler.impl;

import com.andrejanesic.cads.homework1.Main;
import com.andrejanesic.cads.homework1.config.AppConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
@AllArgsConstructor
public class DirectoryCrawlerWorker implements Runnable {

    /**
     * Hash map for all crawlers (only one) to mark files.
     */
    private static final ConcurrentHashMap<String, Long> indexedDirs = new ConcurrentHashMap<>();
    /**
     * The directory that should be crawled.
     */
    @Getter
    @Setter
    private String directory;

    @Override
    public void run() {
        AppConfiguration appConfiguration = Main.getCore().getConfig().getConfig();
        if (appConfiguration == null) {
            //TODO report exception
            return;
        }

        try {
            // BFS through dir tree
            Deque<File> content = new ArrayDeque<>();
            Set<String> visited = new HashSet<>();
            while (directory != null) {
                File curr;

                // if initial path is not a directory, report error
                if (content.isEmpty()) {
                    curr = new File(directory);
                    visited.clear();
                    if (!curr.exists() || !curr.isDirectory()) {
                        //TODO report exception
                        return;
                    }
                } else {
                    curr = content.pop();
                }

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
                    continue;
                }

                // traverse dir
                File[] files = curr.listFiles();
                if (files == null) continue;
                for (File nested : files) {
                    content.addFirst(nested);
                }

                Thread.sleep(appConfiguration.directoryCrawlerSleepTime());
            }
        } catch (Exception e) {
            // TODO if exception
        }
    }

    /**
     * Terminates the thread.
     */
    protected void terminate() {
        directory = null;
    }
}
