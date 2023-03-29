package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.args.commons.ArgsCommonsCLI;
import com.andrejanesic.cads.homework1.config.cfg4j.CFG4JLoader;
import com.andrejanesic.cads.homework1.core.Core;
import lombok.Getter;

public class Main {

    /**
     * Whether the app has been initialized or not.
     */
    private static boolean initialized = false;
    private static final Object initializedLock = new Object();

    @Getter
    private static Core core;

    /**
     * Initializes the program.
     */
    public static void init() {
        if (initialized) return;
        synchronized (initializedLock) {
            if (initialized) return;

            core = new Core(
                    new ArgsCommonsCLI(),
                    new CFG4JLoader()
            );
            initialized = true;
        }
    }

    public static void main(String[] args) {
        init();
        System.out.println("Hello world!");
    }
}