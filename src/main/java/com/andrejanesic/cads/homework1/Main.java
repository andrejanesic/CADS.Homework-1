package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.args.commons.ArgsCommonsCLI;
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
     */
    public static void init() {
        if (initialized) return;
        synchronized (initializedLock) {
            if (initialized) return;

            core = new Core(
                    new ArgsCommonsCLI()
            );
            initialized = true;
        }
    }

    public static void main(String[] args) {
        init();
        System.out.println("Hello world!");
    }
}