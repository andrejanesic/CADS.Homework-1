package com.andrejanesic.cads.homework1.scanner;

import com.andrejanesic.cads.homework1.core.ThreadPoolComponent;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.result.Result;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Scanner component. Uses {@link Result} as its result type.
 */
public abstract class IScanner extends ThreadPoolComponent<Result> {


    /**
     * Map of jobs to their results.
     */
    @Getter
    private final Map<IJob, Future<Result>> jobResults = new HashMap<>();

    public IScanner(ExecutorService pool) {
        super(pool);
    }

    public IScanner() {
    }

    /**
     * Submits a new job for execution by the scanner.
     *
     * @param job
     */
    public abstract Future<Result> submit(IJob job);
}
