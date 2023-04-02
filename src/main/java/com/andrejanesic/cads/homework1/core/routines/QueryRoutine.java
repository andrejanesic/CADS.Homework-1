package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Routine for fetching a query result.
 */
public class QueryRoutine implements IRoutine {

    private final IResultRetriever resultRetriever;
    private final ICLOutput iclOutput;
    private final Query query;

    public QueryRoutine(
            IResultRetriever resultRetriever,
            ICLOutput iclOutput,
            Query query
    ) {
        this.resultRetriever = resultRetriever;
        this.iclOutput = iclOutput;
        this.query = query;
    }


    @Override
    public void doRoutine() throws RoutineException {
        Future<Result> future = resultRetriever.submit(query);
        Result r = null;
        if (query.isWait()) {
            try {
                r = future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RoutineException(e);
            }
        } else {
            try {
                r = future.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException |
                     TimeoutException e) {
                if (!future.isDone()) {
                    iclOutput.warning(
                            "Query still in progress..."
                    );
                    return;
                }
            }
        }
        if (r == null) {
            iclOutput.error("Could not fetch query results");
        }

        String uri = query.getUri() != null ? query.getUri().toString() :
                "N/A";
        String type = query.getType() != null ? query.getType().toString() :
                "N/A";
        String id = query.getId() != null ? query.getId() : "N/A";
        String blocking = query.isWait() ? "yes" : "no";

        StringBuilder exceptions = new StringBuilder();
        if (r.getExceptions() == null) {
            exceptions.append("N/A");
        } else {
            for (Exception e : r.getExceptions()) {
                exceptions.append("\n\t").append(e.getMessage());
            }
        }

        StringBuilder frequency = new StringBuilder();
        if (r.getFrequency() == null) {
            frequency.append("N/A");
        } else {
            r.getFrequency().forEach((k, v) -> {
                frequency.append("\n\t")
                        .append(k + ": " + v);
            });
        }

        String success = r.isSuccess() ? "successful" : "failed (see " +
                "Exceptions)";

        String consumedTime = String.format(
                "%.3f", (r.getConsumedTime() / 1000.0)
        );

        String completedTime =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.mmm")
                        .format(new Date(r.getCompletionTime()));

        String cached = r.isCached() ? "yes" : "no";

        StringBuilder out = new StringBuilder();
        out.append("Query results:")
                .append("\n--- Query info ---")
                .append("\nPattern:  " + uri)
                .append("\nJob ID:   " + id)
                .append("\nJob type: " + type)
                .append("\nBlocking: " + blocking)
                .append("\n--- Results ---")
                .append("\nKeywords:   " + frequency)
                .append("\nSuccess:    " + success)
                .append("\nExceptions: " + exceptions)
                .append("\nConsumed:   " + consumedTime + "s")
                .append("\nDone at:    " + completedTime)
                .append("\nFrom cache: " + cached);
        iclOutput.info(out.toString());
    }

    @Override
    public void undoRoutine() throws RoutineException {

    }
}
