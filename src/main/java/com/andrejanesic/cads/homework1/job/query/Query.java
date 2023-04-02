package com.andrejanesic.cads.homework1.job.query;

import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Submitted to {@link IResultRetriever} for querying job results.
 */
@Builder
@Getter
public class Query {

    /**
     * Query by job ID.
     */
    private String id;
    /**
     * Query by job type.
     */
    private JobType type;
    /**
     * Query by job directory URI: path for FileJob, URL for WebJob.
     */
    private Pattern uri;
    /**
     * Whether the query should wait on the final results or return the current
     * state of the job.
     */
    private boolean wait;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Query query)) return false;
        return isWait() == query.isWait() && Objects.equals(getId(), query.getId()) && getType() == query.getType() && Objects.equals(getUri(), query.getUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getType(),
                getUri() == null ? null : getUri().toString(),
                isWait()
        );
    }
}
