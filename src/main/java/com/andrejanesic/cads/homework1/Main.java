package com.andrejanesic.cads.homework1;

import com.andrejanesic.cads.homework1.core.DaggerICore;
import com.andrejanesic.cads.homework1.core.ICore;

public class Main {

    /**
     * Initializes the program.
     *
     * @param args Passed arguments.
     */
    private static void init(String[] args) {

        // initialize dagger
        ICore core = DaggerICore.create();

        // init components
        try {
            // initialize components
            core.args().parse(args);
            core.config().load();
        } catch (Exception e) {
            // TODO log to cli
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        init(args);
        System.out.println("Hello world!");
    }
}