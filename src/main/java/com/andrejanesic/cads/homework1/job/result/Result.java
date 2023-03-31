package com.andrejanesic.cads.homework1.job.result;

import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.job.IJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.*;

/**
 * Wraps the job result, along with useful data.
 */
@AllArgsConstructor
@Data
public class Result {

    /**
     * The jobs related to this result.
     */
    @NonNull
    private final Set<IJob> jobs = new HashSet<>();
    /**
     * Whether the job completed successfully or encountered an exception.
     */
    private boolean success = false;
    /**
     * If any exceptions were encountered, they are stored in this collection.
     */
    private Collection<ScannerException> exceptions = null;
    /**
     * The keyword frequency counting result.
     */
    private Map<String, Integer> frequency = new HashMap<>();
    /**
     * Timestamp of completion. Zero if not completed yet.
     */
    private long completionTime = 0;
    /**
     * Time it took for the job to complete.
     */
    private long consumedTime = 0;

    /**
     * Wraps the job result, along with useful data.
     *
     * @param jobs the jobs related to this result
     */
    public Result(@NonNull IJob... jobs) {
        this.jobs.addAll(List.of(jobs));
    }

}
