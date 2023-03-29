package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.ArgsException;
import com.andrejanesic.cads.homework1.core.exceptions.ConfigException;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Binds all app components together.
 */
@Builder
@Getter
public class Core {

    @NonNull
    private IArgs args;

    @NonNull
    private IConfig config;

    @NonNull
    private IDirectoryCrawler directoryCrawler;

    /**
     * Initializes the components.
     *
     * @param args Passed arguments.
     */
    public void init(String[] args) throws ArgsException, ConfigException {
        getArgs().parse(args);
        getConfig().load();
    }
}
