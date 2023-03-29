package com.andrejanesic.cads.homework1.utils;

import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Creates a directory in the file system for testing the application.
 */
public class MockDirectoryTree {

    /**
     * Path to the generated directory.
     */
    @Getter
    @NonNull
    private final String path;

    /**
     * Prefix used for special directories.
     */
    @Getter
    @NonNull
    private final String prefix;

    /**
     * Minimum directory depth.
     */
    @Getter
    @NonNull
    private final int minDepth;

    /**
     * Maximum directory depth. May or may not be reached.
     */
    @Getter
    @NonNull
    private final int maxDepth;

    /**
     * Maximum number of items in each directory.
     */
    @Getter
    @NonNull
    private final int maxItemsPerDir;

    /**
     * Set of the absolute URLs of all directories created with the prefix.
     */
    @Getter
    private final Set<String> prefixDirs = new HashSet<>();

    /**
     * Mapper of absolute file paths to their respective {@link MockTextCorpus}.
     */
    @Getter
    private final Map<String, MockTextCorpus> files = new HashMap<>();

    /**
     * Used when creating non-directory items (files).
     */
    @Getter
    @NonNull
    private final MockTextCorpus.Builder mockTextCorpusBuilder;

    /**
     * Creates a directory in the file system for testing the application.
     *
     * @param path                  Path to the generated directory.
     * @param prefix                Prefix used for special directories.
     * @param minDepth              Minimum directory depth.
     * @param maxDepth              Maximum directory depth. May or may not be reached.
     * @param maxItemsPerDir        Maximum number of items per directory.
     * @param mockTextCorpusBuilder Used when creating non-directory items (files). Path variable will be overwritten.
     */
    public MockDirectoryTree(String path, String prefix, int minDepth, int maxDepth, int maxItemsPerDir, MockTextCorpus.Builder mockTextCorpusBuilder) {
        if (maxItemsPerDir < 1 && minDepth > 1)
            throw new RuntimeException(
                    "A multi-level directory (minDepth >= 2) requires maxItemsPerDir >= 1"
            );
        this.path = new File(path).getAbsolutePath();
        this.prefix = prefix;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
        this.maxItemsPerDir = maxItemsPerDir;
        this.mockTextCorpusBuilder = mockTextCorpusBuilder;
    }

    /**
     * Creates a directory in the file system for testing the application.
     */
    public MockDirectoryTree() {
        this(
                "temp-" + System.currentTimeMillis(),
                "pre_",
                1,
                (int) (Math.random() * 4 + 2),
                (int) (Math.random() * 50),
                new MockTextCorpus.Builder(
                        "path",
                        " ",
                        new String[]{},
                        1024
                )
        );
    }

    /**
     * Generates the tree in the file system.
     */
    public void generate() throws IOException {
        populateDir(path, minDepth, maxDepth);
    }

    /**
     * Creates the directory at path and populates with items, if depth is available.
     *
     * @param path           The path to create the directory at.
     * @param depthRequired  Minimum depth required.
     * @param depthAvailable Remaining depth.
     */
    private void populateDir(String path, int depthRequired, int depthAvailable) throws IOException {
        if (depthAvailable == 0) return;
        File created = new File(path);
        created.mkdirs();
        path = created.getAbsolutePath();
        int items = maxItemsPerDir;

        // create one dir to meet minimum depth requirement
        if (depthRequired > 0) {
            items -= 1;
            populateDir(
                    path + File.separator + "dir-0",
                    depthRequired - 1,
                    depthAvailable - 1
            );
        }

        // populate the rest of the items randomly
        for (int i = (depthRequired > 0 ? 1 : 0); i < (int) (Math.random() * items); i++) {
            int r = (int) (Math.random() * 100);
            String newPath;

            // dir
            if (r < 60) {
                boolean prefixDir = false;
                if (r < 30) {
                    newPath = path + File.separator + "dir-" + i;
                } else {
                    newPath = path + File.separator + prefix + "dir-" + i;
                    prefixDir = true;
                }

                if (depthAvailable > 1) {
                    populateDir(newPath, depthRequired - 1, depthAvailable - 1);
                    if (prefixDir) {
                        prefixDirs.add(newPath);
                    }
                }
                continue;
            }

            // file
            newPath = path + File.separator + "f-" + i + ".txt";
            mockTextCorpusBuilder.setPath(newPath);
            MockTextCorpus mockFile = new MockTextCorpus(mockTextCorpusBuilder);
            mockFile.generate();
            files.put(newPath, mockFile);
        }
    }

    /**
     * Deletes the tree from the system.
     */
    public void remove() throws IOException {
        Files.walk(Path.of(path))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
