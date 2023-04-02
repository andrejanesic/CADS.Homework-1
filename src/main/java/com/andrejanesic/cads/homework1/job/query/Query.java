package com.andrejanesic.cads.homework1.job.query;

import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Submitted to {@link IResultRetriever} for querying job results.
 */
@Builder
@Getter
@Setter
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
     *
     * @deprecated use {@link #uris)} instead.
     */
    private Pattern uri;
    /**
     * If searching for multiple URIs.
     */
    private Set<Pattern> uris;
    /**
     * Whether the query should wait on the final results or return the current
     * state of the job.
     */
    private boolean wait;

    /**
     * Helper method for calculating the hash code of Patterns.
     *
     * @return array of strings which represent patterns from {@link #uris}
     */
    private String[] getUriStrings() {
        String[] uriStrings;
        if (uris != null) {
            uriStrings = new String[uris.size()];
            int i = 0;
            for (Pattern p : uris) {
                uriStrings[i] = p.toString();
            }
        } else {
            uriStrings = new String[0];
        }
        return uriStrings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Query query)) return false;
        return isWait() == query.isWait() && Objects.equals(getId(), query.getId()) && getType() == query.getType() && Objects.equals(getUri(), query.getUri()) && Objects.equals(getUris(), query.getUris());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getType(),
                getUri() == null ? null : getUri().toString(),
                Arrays.hashCode(getUriStrings()),
                isWait()
        );
    }
}
