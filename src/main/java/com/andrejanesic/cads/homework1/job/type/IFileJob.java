package com.andrejanesic.cads.homework1.job.type;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import lombok.Getter;

public abstract class IFileJob extends IJob {

    /**
     * Path to the file that should be scanned.
     */
    @Getter
    private final String path;

    /**
     * File scanning job.
     *
     * @param path Path to the file that should be scanned.
     */
    public IFileJob(String path) {
        super(JobType.FILE);
        this.path = path;
    }
}
