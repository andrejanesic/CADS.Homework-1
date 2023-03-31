package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.core.DaggerICore;
import com.andrejanesic.cads.homework1.core.IComponent;
import com.andrejanesic.cads.homework1.core.ICore;

import java.util.List;

public class Main {

    private static ICore core;

    /**
     * Initializes the program. Starts each component.
     *
     * @param args Passed arguments.
     */
    private static void start(String[] args) {

        // initialize dagger
        core = DaggerICore.create();

        // start components
        try {
            // output module
            core.clOutput().start();

            // parse arguments
            core.args().start();
            core.args().parse(args);

            // start the config
            core.config().start();
            core.config().load();

            // start job queue and dispatcher
            core.jobQueue().start();
            core.jobDispatcher().start();

            // start crawler and scanners
            core.directoryCrawler().start();
            core.fileScanner().start();
            core.webScanner().start();

            // start ui
            core.clInput().start();

        } catch (Exception e) {
            // TODO handle exceptions
        }
    }

    public static void end() {

    }

    public static void main(String[] args) {
        start(args);
    }
}