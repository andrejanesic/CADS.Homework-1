package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;

import java.util.Map;
import java.util.concurrent.Future;

public class MockJob implements IJob {

    private final long id = System.currentTimeMillis();

    @Override
    public long getId() {
        return id;
    }

    @Override
    public JobType getType() {
        return null;
    }

    @Override
    public Future<Map<String, Integer>> start() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IJob) &&
                ((IJob) obj).getId() == getId() &&
                ((IJob) obj).getType().equals(getType());
    }
}
