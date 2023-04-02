package com.andrejanesic.cads.homework1.job.type;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.scanner.IScanner;

/**
 * Stops the scanner component it is submitted to.
 * <p>
 * An IScanner component must stop all functioning having received an instance
 * of this job. Any thread being run by the IScanner should gracefully end.
 *
 * @see IScanner
 */
public class StopJob extends IJob {

    /**
     * Default constructor.
     *
     * @param type the job type, indicating which scanner the job should be
     *             sent to
     */
    public StopJob(JobType type) {
        super(type);
    }

}
