package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.core.DaggerICore;
import com.andrejanesic.cads.homework1.core.IComponent;
import com.andrejanesic.cads.homework1.core.ThreadedComponent;
import com.andrejanesic.cads.homework1.core.ICore;

import java.util.HashSet;
import java.util.Set;

public class Main {

    /**
     * Initializes the program.
     *
     * @param args Passed arguments.
     */
    private static void init(String[] args) {

        // initialize dagger
        ICore core = DaggerICore.create();
        Set<IComponent> components = new HashSet<>() {{
            add(core.args());
            add(core.cli());
            add(core.config());
            add(core.directoryCrawler());
            add(core.jobQueue());
            add(core.jobDispatcher());
            add(core.fileScanner());
            add(core.webScanner());
        }};

        // init components
        try {
            // start components

            // initialize components
            core.args().parse(args);
            core.config().load();
            core.cli().main();
        } catch (Exception e) {
        }
    }

    private static void startComponents(ICore core) {
        try {
            core.cli().start();
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        init(args);
    }
}