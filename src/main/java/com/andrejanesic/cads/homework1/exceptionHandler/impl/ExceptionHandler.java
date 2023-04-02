package com.andrejanesic.cads.homework1.exceptionHandler.impl;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ExceptionHandler extends IExceptionHandler {

    private final ICLOutput clOutput;

    /**
     * Default constructor.
     *
     * @param clOutput command line output interface
     */
    @Inject
    public ExceptionHandler(ICLOutput clOutput) {
        this.clOutput = clOutput;
    }

    @Override
    public void handle(Exception e) {
        if (this.clOutput == null) {
            System.out.println(e.getMessage());
            return;
        }

        clOutput.error(e.getMessage());
    }

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {

    }

    @Override
    public void beforeEnd() {

    }
}
