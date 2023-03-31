package com.andrejanesic.cads.homework1.scanner.file;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

@Singleton
public class FileScanner extends IFileScanner {

    private final IResultRetriever resultRetriever;
    private final IJobQueue jobQueue;
    private final IConfig config;

    @Inject
    public FileScanner(
            IResultRetriever resultRetriever,
            IJobQueue jobQueue,
            IConfig config
    ) {
        super(new ForkJoinPool());
        this.resultRetriever = resultRetriever;
        this.jobQueue = jobQueue;
        this.config = config;
    }

    @Override
    public Future<Result> submit(IJob job) {
        if (!(job instanceof FileJob fileJob))
            throw new RuntimeComponentException("Invalid job type passed");

        // number of threads per
        List<String> filePaths = new LinkedList<>();
        File srcDir = new File(fileJob.getPath());
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            // TODO this could be handled better
            return null;
        }

        File[] files = srcDir.listFiles();
        if (files == null || files.length == 0) {
            // TODO this could be handled better
            return null;
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
        resultRetriever.getStoreFileJobs().put(job, res);
        return res;
    }

    @Override
    public void main() {
        keepAlive();
    }
}
