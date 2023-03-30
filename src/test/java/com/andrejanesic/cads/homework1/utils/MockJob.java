package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import lombok.NonNull;

public class MockJob extends IJob {

    public MockJob(@NonNull JobType type) {
        super(type);
    }

}
