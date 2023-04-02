package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.core.DaggerICore;
import com.andrejanesic.cads.homework1.core.ICore;
import lombok.Getter;
import lombok.Setter;

public class Main {

    /**
     * Lock for performing synchronized actions on {@link #isEnd()}.
     */
    @Getter
    private static final Object endLock = new Object();
    private static ICore core;
    /**
     * Whether the program should end or not. Set to true from any thread to
     * start ending sequence.
     */
    @Setter
    @Getter
    private static boolean end;

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
            // exception handler
            core.exceptionHandler().start();

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

    /**
     * Stops all components, calling the end lifecycle.
     */
    public static void end() {
        core.clInput().end();
        synchronized (core.jobQueue()) {
            core.jobQueue().notifyAll();
            core.jobQueue().end();
        }
        core.directoryCrawler().end();
        core.jobDispatcher().end();
        core.webScanner().end();
        core.fileScanner().end();
        core.jobQueue().end();
        core.config().end();
        core.args().end();
        core.clOutput().end();
        core.exceptionHandler().end();
    }

    public static void main(String[] args) {
        start(args);
        synchronized (endLock) {
            while (!end) {
                try {
                    endLock.wait();
                } catch (InterruptedException e) {
                    ;
                }
            }
            end();
        }
    }
}