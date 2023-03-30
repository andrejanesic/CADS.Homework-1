package com.andrejanesic.cads.homework1.scanner;

import com.andrejanesic.cads.homework1.scanner.file.FileScanner;
import com.andrejanesic.cads.homework1.scanner.web.WebScanner;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface IScannerModule {

    @Binds
    @Singleton
    IFileScanner bindFileScanner(WebScanner impl);

    @Binds
    @Singleton
    IWebScanner bindWebScanner(FileScanner impl);
}
