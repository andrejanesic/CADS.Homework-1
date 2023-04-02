package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;

/**
 * Routine for adding a new directory to be scanned by
 * {@link IDirectoryCrawler}.
 */
public class AddDirectoryRoutine implements IRoutine {

    private final ICLOutput clOutput;
    private final IDirectoryCrawler directoryCrawler;
    private final String path;

    /**
     * Default constructor.
     *
     * @param clOutput         output interface
     * @param directoryCrawler directory crawler
     * @param path             directory path. Can be relative or absolute.
     */
    public AddDirectoryRoutine(
            ICLOutput clOutput,
            IDirectoryCrawler directoryCrawler,
            String path
    ) {
        this.clOutput = clOutput;
        this.directoryCrawler = directoryCrawler;
        this.path = path;
    }

    @Override
    public void doRoutine() throws RoutineException {
        directoryCrawler.crawl(path);
    }

    @Override
    public void undoRoutine() throws RoutineException {

    }
}
