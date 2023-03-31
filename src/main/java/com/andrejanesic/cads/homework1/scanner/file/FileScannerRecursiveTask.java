package com.andrejanesic.cads.homework1.scanner.file;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.utils.KeywordCounter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.RecursiveTask;

@AllArgsConstructor
public class FileScannerRecursiveTask extends RecursiveTask<Result> {

    @NonNull
    private final FileJob job;
    @NonNull
    private final List<String> filePaths;
    private final int from;
    private final int to;
    @NonNull
    private final IJobQueue jobQueue;
    @NonNull
    private final IConfig config;

    @Override
    protected Result compute() {
        long startTime = System.currentTimeMillis();
        Result result = new Result(job);
        Collection<ScannerException> exceptions = new LinkedList<>();
        result.setExceptions(exceptions);

        // split passed docs until totalSize < limit
        long totalSize = 0;
        for (String filePath : filePaths.subList(from, to)) {
            try {
                totalSize += Files.size(Paths.get(filePath));
            } catch (IOException e) {
                exceptions.add(new ScannerException(e));
            }
        }

        // if too large, fork-join
        if (totalSize > config.getConfig().fileScanningSizeLimit()) {

            // if single file too large, error and return
            if (filePaths.subList(from, to).size() == 1) {

                // check if directory detected
                File f = new File(filePaths.subList(from, to).get(0));
                if (f.isDirectory()) {

                    FileJob newJob = new FileJob(
                            f.getAbsolutePath()
                    );
                    try {
                        jobQueue.enqueueJob(newJob);
                        job.addChild(newJob);
                    } catch (JobQueueException e) {
                        exceptions.add(new ScannerException(e));
                    }

                    result.setSuccess(true);
                    result.setFrequency(new HashMap<>());
                    long endTime = System.currentTimeMillis();
                    result.setCompletionTime(endTime);
                    result.setConsumedTime(endTime - startTime);
                    return result;
                }

                exceptions.add(new ScannerException(
                        "File " + filePaths.subList(from, to).get(0) +
                                " is too large for parsing."
                ));
                result.setSuccess(false);
                result.setFrequency(new HashMap<>());
                long endTime = System.currentTimeMillis();
                result.setCompletionTime(endTime);
                result.setConsumedTime(endTime - startTime);
                return result;
            }

            int mid = from + (to - from) / 2;
            FileScannerRecursiveTask left = new FileScannerRecursiveTask(
                    job,
                    filePaths,
                    from,
                    mid,
                    jobQueue,
                    config
            );
            FileScannerRecursiveTask right = new FileScannerRecursiveTask(
                    job,
                    filePaths,
                    mid,
                    to,
                    jobQueue,
                    config
            );

            left.fork();
            Result rightResult = right.compute();
            Result leftResult = left.join();

            // collect exceptions
            if (leftResult.getExceptions() != null)
                exceptions.addAll(leftResult.getExceptions());
            if (rightResult.getExceptions() != null)
                exceptions.addAll(rightResult.getExceptions());
            result.setExceptions(exceptions);
            result.setSuccess(exceptions.isEmpty());

            // collect frequency
            Map<String, Integer> frequency = new HashMap<>();
            if (leftResult.getFrequency() != null)
                leftResult.getFrequency().forEach((k, v) -> {
                    frequency.put(k, frequency.getOrDefault(k, 0) + v);
                });
            if (rightResult.getFrequency() != null)
                rightResult.getFrequency().forEach((k, v) -> {
                    frequency.put(k, frequency.getOrDefault(k, 0) + v);
                });
            result.setFrequency(frequency);

            long endTime = System.currentTimeMillis();
            result.setCompletionTime(endTime);
            result.setConsumedTime(endTime - startTime);
            return result;
        }

        // not too large, compute
        StringBuilder sb = new StringBuilder();
        for (String path : filePaths.subList(from, to)) {
            try {
                File f = new File(path);
                if (!f.exists())
                    continue;

                // if subdirectory, submit as job
                if (f.isDirectory()) {

                    FileJob newJob = new FileJob(
                            f.getAbsolutePath()
                    );
                    try {
                        jobQueue.enqueueJob(newJob);
                        job.addChild(newJob);
                    } catch (JobQueueException e) {
                        exceptions.add(new ScannerException(e));
                    }
                    continue;
                }

                BufferedReader reader = new BufferedReader(
                        new FileReader(f.getAbsolutePath())
                );
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
            } catch (IOException e) {
                exceptions.add(new ScannerException(e));
            }
        }

        // count keywords
        String text = sb.toString();
        String delimiter = config.getConfig().delimiter();
        Map<String, Integer> frequencyRaw = null;
        try {
            //noinspection OptionalGetWithoutIsPresent
            frequencyRaw = KeywordCounter.getFrequency(
                    text,
                    delimiter
            );
        } catch (RuntimeException e) {
            exceptions.add(new ScannerException(e));
        }
        result.setSuccess(exceptions.isEmpty());
        result.setFrequency(frequencyRaw);
        long endTime = System.currentTimeMillis();
        result.setCompletionTime(endTime);
        result.setConsumedTime(endTime - startTime);
        return result;
    }
}
