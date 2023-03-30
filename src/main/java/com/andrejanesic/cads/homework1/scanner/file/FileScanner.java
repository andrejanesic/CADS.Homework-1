package com.andrejanesic.cads.homework1.scanner.file;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;

import javax.inject.Inject;
import java.util.concurrent.Future;

public class FileScanner extends IWebScanner {

    @Inject
    public FileScanner() {
    }

    @Override
    public Future<Result> submit(IJob job) {
        return null;
    }
}
