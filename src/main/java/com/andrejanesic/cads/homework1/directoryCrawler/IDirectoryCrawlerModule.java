package com.andrejanesic.cads.homework1.directoryCrawler;

import com.andrejanesic.cads.homework1.directoryCrawler.impl.DirectoryCrawler;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface IDirectoryCrawlerModule {

    @Binds
    @Singleton
    IDirectoryCrawler bindDirectoryCrawler(DirectoryCrawler impl);

}
