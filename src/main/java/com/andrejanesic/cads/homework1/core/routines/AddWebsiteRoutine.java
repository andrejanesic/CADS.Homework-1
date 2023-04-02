package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;

/**
 * Routine for adding a new website/URL to be scanned by
 * {@link #}
 */
public class AddWebsiteRoutine implements IRoutine {

    private final IWebScanner webScanner;
    private final IConfig config;
    private final String url;

    public AddWebsiteRoutine(
            IWebScanner webScanner,
            IConfig config,
            String url
    ) {
        this.webScanner = webScanner;
        this.config = config;
        this.url = url;
    }

    @Override
    public void doRoutine() throws RoutineException {
        this.webScanner.submit(
                new WebJob(url, config.getConfig().hopCount())
        );
    }

    @Override
    public void undoRoutine() throws RoutineException {

    }
}
