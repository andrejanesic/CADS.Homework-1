package com.andrejanesic.cads.homework1.scanner.web;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.utils.KeywordCounter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class WebScannerCallable implements Callable<Result> {

    @NonNull
    private final WebJob job;
    @NonNull
    private final IJobQueue jobQueue;
    @NonNull
    private final IConfig config;

    @Override
    public Result call() throws Exception {
        long startTime = System.currentTimeMillis();
        Result result = new Result(job);
        Collection<ScannerException> exceptions = new LinkedList<>();
        result.setExceptions(exceptions);

        // fetch document
        Document document = null;
        try {
            document = Jsoup.connect(job.getUrl()).get();
            if (document == null || document.text().length() == 0)
                throw new ScannerException("document is null or no text");
        } catch (Exception e) {
            exceptions.add(new ScannerException(e));
        }

        // if error when fetching
        if (!exceptions.isEmpty()) {
            long endTime = System.currentTimeMillis();
            result.setSuccess(false);
            result.setCompletionTime(endTime);
            result.setConsumedTime(endTime - startTime);
            return result;
        }

        // if still hopping, fetch urls and submit new jobs
        if (job.getHops() > 0) {
            Elements links = document.select("a[href]");
            links.forEach(link -> {
                String url = link.attr("href");
                if (url == null || url.length() < 1) return;

                // add new job
                WebJob newJob = new WebJob(
                        url,
                        job.getHops() - 1
                );
                try {
                    jobQueue.enqueueJob(newJob);
                    job.addChild(newJob);
                } catch (JobQueueException e) {
                    exceptions.add(new ScannerException(e));
                }
            });
        }

        // fetch all text and count keywords
        String text = document.text();
        String delimiter = config.getConfig().delimiter();
        Map<String, Integer> frequencyRaw = null;
        try {
            //noinspection OptionalGetWithoutIsPresent
            frequencyRaw = KeywordCounter.getFrequency(
                    text,
                    delimiter
            );
        } catch (Exception e) {
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
