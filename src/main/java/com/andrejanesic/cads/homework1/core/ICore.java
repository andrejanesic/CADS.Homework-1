package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.args.IArgsModule;
import com.andrejanesic.cads.homework1.cli.input.ICLInput;
import com.andrejanesic.cads.homework1.cli.input.ICLInputModule;
import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.cli.output.ICLOutputModule;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.config.IConfigModule;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawlerModule;
import com.andrejanesic.cads.homework1.job.dispatcher.IJobDispatcher;
import com.andrejanesic.cads.homework1.job.dispatcher.IJobDispatcherModule;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.queue.IJobQueueModule;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetrieverModule;
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
        ICLInputModule.class,
        ICLOutputModule.class,
        IScannerModule.class,
        IResultRetrieverModule.class,
})
public interface ICore {

    IArgs args();

    IConfig config();

    IDirectoryCrawler directoryCrawler();

    IJobQueue jobQueue();

    IJobDispatcher jobDispatcher();

    ICLInput clInput();

    ICLOutput clOutput();

    IFileScanner fileScanner();

    IWebScanner webScanner();

    IResultRetriever resultRetriever();
}
