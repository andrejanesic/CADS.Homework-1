package com.andrejanesic.cads.homework1.job.query;

import com.andrejanesic.cads.homework1.job.JobType;
import lombok.Builder;
import lombok.Getter;

/**
 * Used for querying job results.
 */
@Builder
@Getter
public class Query {

    private String id;
    private JobType type;
    private String url;
    private String path;
}
