package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.core.DaggerICore;
import com.andrejanesic.cads.homework1.core.IComponent;
import com.andrejanesic.cads.homework1.core.ICore;

import java.util.List;

public class Main {

    /**
     * Initializes the program. Starts each component.
     *
     * @param args Passed arguments.
     */
    private static void start(String[] args) {

        // initialize dagger
        ICore core = DaggerICore.create();

        // start components
        try {
            // parse arguments
            core.args().start();
            core.args().parse(args);

            // start the config
            core.config().start();
            core.config().load();

            // start cli
            core.cli().start();

            // start job queue and dispatcher
            core.jobQueue().start();
            core.jobDispatcher().start();

            // start crawler and scanners
            core.directoryCrawler().start();
            core.fileScanner().start();
            core.webScanner().start();

        } catch (Exception e) {
            // TODO handle exceptions
        }
    }

    private static void startComponents(List<IComponent> components) {
        try {
            for (IComponent c : components) {
                c.start();
            }
        } catch (Exception e) {
            // TODO catch exceptions
        }
    }

    private static void endComponents(List<IComponent> components) {
        try {
            for (IComponent c : components) {
                c.end();
            }
        } catch (Exception e) {
            // TODO catch exceptions
        }
    }

    public static void main(String[] args) {
        start(args);
    }
}