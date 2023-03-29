package com.andrejanesic.cads.homework1.directoryCrawler;

import com.andrejanesic.cads.homework1.directoryCrawler.impl.DirectoryCrawler;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class IDirectoryCrawlerModule {

    @Provides
    @Singleton
    IDirectoryCrawler provideDirectoryCrawler() {
        return new DirectoryCrawler();
    }

}
