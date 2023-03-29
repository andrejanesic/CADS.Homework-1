package com.andrejanesic.cads.homework1.cli.commons;

import com.andrejanesic.cads.homework1.cli.ICLI;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;

import javax.inject.Inject;

public class CLICommons extends ICLI {

    private InputReader in;
    private OutputWriter out;

    @Inject
    public CLICommons() {
    }

    @Override
    public void init() {
        super.init();

        in = new InputReader();
        out = new OutputWriter();

        // start
        fork(in);
    }

    @Override
    public void info(String message) {
        if (out == null)
            throw new RuntimeComponentException("CLICommons not available yet");
        out.log(OutputWriter.MessageType.INFO, message);
    }

    @Override
    public void warning(String message) {
        if (out == null)
            throw new RuntimeComponentException("CLICommons not available yet");
        out.log(OutputWriter.MessageType.WARNING, message);
    }

    @Override
    public void error(String message) {
        if (out == null)
            throw new RuntimeComponentException("CLICommons not available yet");
        out.log(OutputWriter.MessageType.ERROR, message);
    }
}
