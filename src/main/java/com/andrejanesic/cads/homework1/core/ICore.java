package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.args.IArgsModule;
import com.andrejanesic.cads.homework1.cli.ICLI;
import com.andrejanesic.cads.homework1.cli.ICLIModule;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.config.IConfigModule;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawlerModule;
import com.andrejanesic.cads.homework1.job.dispatcher.IJobDispatcher;
import com.andrejanesic.cads.homework1.job.dispatcher.IJobDispatcherModule;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.queue.IJobQueueModule;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;
import com.andrejanesic.cads.homework1.scanner.IScannerModule;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Binds all app components together.
 */
@Singleton
@Component(modules = {
        IArgsModule.class,
        IConfigModule.class,
        IDirectoryCrawlerModule.class,
        IJobQueueModule.class,
        IJobDispatcherModule.class,
        ICLIModule.class,
        IScannerModule.class,
})
public interface ICore {

    IArgs args();

    IConfig config();

    IDirectoryCrawler directoryCrawler();

    IJobQueue jobQueue();

    IJobDispatcher jobDispatcher();

    ICLI cli();

    IFileScanner fileScanner();

    IWebScanner webScanner();
}
