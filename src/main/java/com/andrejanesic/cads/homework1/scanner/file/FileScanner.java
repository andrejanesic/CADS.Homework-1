package com.andrejanesic.cads.homework1.scanner.file;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Pattern;

@Singleton
public class FileScanner extends IFileScanner {

    private final IResultRetriever resultRetriever;
    private final IJobQueue jobQueue;
    private final IConfig config;
    private final IExceptionHandler exceptionHandler;
    private final ICLOutput clOutput;

    /**
     * @param resultRetriever result retriever
     * @param jobQueue        job queue
     * @param config          config
     * @deprecated use the new default constructor:
     * {@link #FileScanner(IResultRetriever, IJobQueue, IConfig, IExceptionHandler, ICLOutput)}
     */
    public FileScanner(
            IResultRetriever resultRetriever,
            IJobQueue jobQueue,
            IConfig config
    ) {
        this(resultRetriever, jobQueue, config, null, null);
    }

    /**
     * Default constructor.
     *
     * @param resultRetriever  result retriever
     * @param jobQueue         job queue
     * @param config           config
     * @param exceptionHandler exception handler
     */
    @Inject
    public FileScanner(
            IResultRetriever resultRetriever,
            IJobQueue jobQueue,
            IConfig config,
            IExceptionHandler exceptionHandler,
            ICLOutput clOutput
    ) {
        super(new ForkJoinPool());
        this.resultRetriever = resultRetriever;
        this.jobQueue = jobQueue;
        this.config = config;
        this.exceptionHandler = exceptionHandler;
        this.clOutput = clOutput;
    }

    @Override
    public Future<Result> submit(IJob job) {
        if (job == null) {
            ScannerException e = new ScannerException(
                    "FileScanner::submit job cannot be null"
            );
            if (exceptionHandler == null)
                throw new RuntimeComponentException(e);
            exceptionHandler.handle(e);
        }
        if (!(job instanceof FileJob)) {
            ScannerException e = new ScannerException(
                    "FileScanner::submit job must be of type FileJob"
            );
            if (exceptionHandler == null)
                throw new RuntimeComponentException(e);
            exceptionHandler.handle(e);
        }
        FileJob fileJob = (FileJob) job;
        long startTime = System.currentTimeMillis();

        // number of threads per
        List<String> filePaths = new LinkedList<>();
        File srcDir = new File(fileJob.getPath());
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            Result r = new Result();
            r.setFrequency(new HashMap<>());
            r.setSuccess(false);
            r.setExceptions(new HashSet<>());
            long endTime = System.currentTimeMillis();
            r.setConsumedTime(endTime - startTime);
            r.setCompletionTime(endTime);
            r.getExceptions().add(
                    new ScannerException(
                            "The path " +
                                    srcDir +
                                    " does not exist or is not a valid " +
                                    "directory"
                    )
            );
            return new Future<>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return true;
                }

                @Override
                public boolean isCancelled() {
                    return true;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public Result get() throws InterruptedException, ExecutionException {
                    return r;
                }

                @Override
                public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return r;
                }
            };
        }

        File[] files = srcDir.listFiles();
        if (files == null || files.length == 0) {
            Result r = new Result();
            r.setFrequency(new HashMap<>());
            r.setSuccess(false);
            r.setExceptions(new HashSet<>());
            long endTime = System.currentTimeMillis();
            r.setConsumedTime(endTime - startTime);
            r.setCompletionTime(endTime);
            r.getExceptions().add(
                    new ScannerException(
                            "The path " +
                                    srcDir +
                                    " does not contain any files, or files " +
                                    "could not be indexed"
                    )
            );
            return new Future<>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return true;
                }

                @Override
                public boolean isCancelled() {
                    return true;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public Result get() throws InterruptedException, ExecutionException {
                    return r;
                }

                @Override
                public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return r;
                }
            };
        }

        for (File f : files) {
            String absPath = f.getAbsolutePath();
            filePaths.add(absPath);
        }

        FileScannerRecursiveTask scanner = new FileScannerRecursiveTask(
                fileJob,
                filePaths,
                0,
                filePaths.size(),
                jobQueue,
                config
        );
        Future<Result> res = ((ForkJoinPool) getPool()).submit(scanner);
        getJobResults().put(job, res);
        resultRetriever.invalidateStores(
                Query.builder()
                        .type(JobType.FILE)
                        .uri(Pattern.compile(srcDir.getName()))
                        .build()
        );
        resultRetriever.getStoreFileJobs().putIfAbsent(job.getId(),
                new IResultRetriever.IJobFutureResult(job, res));
        if (srcDir.getName().startsWith(
                config.getConfig().fileCorpusPrefix())) {
            resultRetriever.getIndexedDirectories().putIfAbsent(
                    srcDir.getName(),
                    true
            );
        }
        return res;
    }

    @Override
    public void main() {
        keepAlive();
    }
}
