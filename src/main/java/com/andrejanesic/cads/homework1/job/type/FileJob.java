package com.andrejanesic.cads.homework1.job.type;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import lombok.Getter;

public class FileJob extends IJob {

    /**
     * Path to the directory that should be scanned.
     */
    @Getter
    private final String path;

    /**
     * File scanning job.
     *
     * @param path Path to the directory that should be scanned.
     */
    public FileJob(String path) {
        super(JobType.FILE);
        this.path = path;
    }
}
