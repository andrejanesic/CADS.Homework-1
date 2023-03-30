package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import lombok.NonNull;

import java.util.Map;
import java.util.concurrent.Future;

public class MockJob extends IJob {

    public MockJob(@NonNull JobType type) {
        super(type);
    }

    @Override
    public Future<Map<String, Integer>> start() {
        return null;
    }

}
