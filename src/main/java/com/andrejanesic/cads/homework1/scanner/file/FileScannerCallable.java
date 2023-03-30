package com.andrejanesic.cads.homework1.scanner.file;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.utils.KeywordCounter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

public class FileScannerCallable implements Callable<Result> {

    private final WebJob job;
    private final IJobQueue jobQueue;
    private final IConfig config;

    public FileScannerCallable(WebJob job, IJobQueue jobQueue, IConfig config) {
        this.job = job;
        this.jobQueue = jobQueue;
        this.config = config;
    }

    @Override
    public Result call() throws Exception {
        long startTime = System.currentTimeMillis();
        // fetch document
        Document document = Jsoup.connect(job.getUrl()).get();
        Result result = new Result(job);

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
                } catch (JobQueueException e) {
                    throw new RuntimeComponentException(e);
                }
            });
        }

        // fetch all text and count keywords
        String text = document.text();
        String delimiter = config.getConfig().delimiter();
        RuntimeException e = null;
        Map<String, Integer> frequencyRaw = null;
        try {
            //noinspection OptionalGetWithoutIsPresent
            frequencyRaw = KeywordCounter.getFrequency(
                    text,
                    delimiter
            );
        } catch (RuntimeException ex) {
            e = ex;
        }
        result.setSuccess(e == null);
        result.setException(e != null ? new ScannerException(e) : null);
        result.setFrequency(frequencyRaw);
        long endTime = System.currentTimeMillis();
        result.setCompletionTime(endTime);
        result.setConsumedTime(endTime - startTime);
        return result;
    }
}
