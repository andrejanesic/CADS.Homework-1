package com.andrejanesic.cads.homework1.job.result;

import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.job.IJob;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Wraps the job result, along with useful data.
 */
@AllArgsConstructor
@Data
public class Result {

    /**
     * The job related to this result.
     */
    @NonNull
    private final IJob job;
    /**
     * Whether the job completed successfully or encountered an exception.
     */
    private boolean success = false;
    /**
     * If an exception was encountered, it is stored here.
     */
    private ScannerException exception = null;
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
     * @param job The job of which this is the result.
     */
    public Result(@NonNull IJob job) {
        this.job = job;
    }

}
