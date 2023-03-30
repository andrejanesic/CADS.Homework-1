package com.andrejanesic.cads.homework1.job;

import java.util.Map;
import java.util.concurrent.Future;

public interface IJob {

    /**
     * Unique, random ID of the job.
     *
     * @return Job ID.
     */
    long getId();

    /**
     * Returns the job type.
     *
     * @return {@link JobType}.
     */
    JobType getType();

    /**
     * Launches the job thread.
     *
     * @return Future job result.
     */
    Future<Map<String, Integer>> start();
}
