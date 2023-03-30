package com.andrejanesic.cads.homework1.job;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public abstract class IJob {

    /**
     * Unique, random ID of the job.
     */
    @Getter
    private final String id = System.currentTimeMillis() + ""
            + ((int) (Math.random() * 1000) + 1);

    /**
     * Type of the job.
     */
    @Getter
    @NonNull
    private final JobType type;

    /**
     * Result of the job.
     */
    @Getter
    @Setter
    private Future<Map<String, Integer>> result;

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IJob) &&
                ((IJob) obj).getId().equals(getId()) &&
                ((IJob) obj).getType().equals(getType());
    }
}
