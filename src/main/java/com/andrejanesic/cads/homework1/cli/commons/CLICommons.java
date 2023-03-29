package com.andrejanesic.cads.homework1.cli.commons;

import com.andrejanesic.cads.homework1.cli.ICLI;

public class CLICommons extends ICLI {

    @Override
    public void init() {
        super.init();

        InputReader in = new InputReader();
        OutputWriter out = new OutputWriter();

        // start
        fork(in);
    }
}
