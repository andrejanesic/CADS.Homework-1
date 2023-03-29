package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.args.IArgsModule;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.config.IConfigModule;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawlerModule;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Binds all app components together.
 */
@Singleton
@Component(modules = {
        IArgsModule.class,
        IConfigModule.class,
        IDirectoryCrawlerModule.class
})
public interface ICore {

    IArgs args();

    IConfig config();

    IDirectoryCrawler directoryCrawler();

}
