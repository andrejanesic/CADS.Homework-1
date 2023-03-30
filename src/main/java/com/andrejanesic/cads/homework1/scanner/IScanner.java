package com.andrejanesic.cads.homework1.scanner;

import com.andrejanesic.cads.homework1.core.ThreadPoolComponent;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.result.Result;

/**
 * Scanner component. Uses {@link Result} as its result type.
 */
public abstract class IScanner extends ThreadPoolComponent<Result> {

    /**
     * Submits a new job for execution by the scanner.
     *
     * @param job
     */
    public abstract void submit(IJob job);

}
