package com.andrejanesic.cads.homework1.scanner;

import java.util.concurrent.ExecutorService;

public abstract class IFileScanner extends IScanner {

    public IFileScanner(ExecutorService pool) {
        super(pool);
    }

    public IFileScanner() {
    }
}
