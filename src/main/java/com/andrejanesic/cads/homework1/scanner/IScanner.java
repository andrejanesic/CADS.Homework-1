package com.andrejanesic.cads.homework1.scanner;

import com.andrejanesic.cads.homework1.core.IComponent;
import com.andrejanesic.cads.homework1.job.IJob;

public abstract class IScanner extends IComponent {

    /**
     * Submits a new job for execution by the scanner.
     *
     * @param job
     */
    public abstract void submit(IJob job);

}
