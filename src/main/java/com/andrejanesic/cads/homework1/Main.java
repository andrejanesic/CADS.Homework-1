package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.args.commons.ArgsCommonsCLI;
import com.andrejanesic.cads.homework1.config.cfg4j.CFG4JLoader;
import com.andrejanesic.cads.homework1.core.Core;
import lombok.Getter;

public class Main {

    private static final Object initializedLock = new Object();
    /**
     * Whether the app has been initialized or not.
     */
    private static boolean initialized = false;

    @Getter
    private static Core core;

    /**
     * Initializes the program.
     *
     * @param args Passed arguments.
     */
    public static void init(String[] args) {
        if (initialized) return;
        synchronized (initializedLock) {
            if (initialized) return;

            // create core
            core = Core.builder()
                    .args(new ArgsCommonsCLI())
                    .configLoader(new CFG4JLoader())
                    .build();

            try {
                // initialize components
                core.init(args);
            } catch (Exception e) {
                System.out.println(e);
            }
            initialized = true;
        }
    }

    /**
     * Initializes the program with no passed arguments.
     */
    public static void init() {
        init(new String[]{});
    }

    public static void main(String[] args) {
        init(args);
        System.out.println("Hello world!");
    }
}