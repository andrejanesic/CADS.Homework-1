package com.andrejanesic.cads.homework1.config.impl;

import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.config.IConfigLoader;
import com.andrejanesic.cads.homework1.constants.IConstants;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.nio.file.Paths;
import java.util.List;

public class ConfigLoader extends IConfigLoader {

    private final Object configurationLock = new Object();
    private AppConfiguration configuration;

    @Override
    public AppConfiguration load() {
        if (configuration != null) {
            return configuration;
        }

        synchronized (configurationLock) {
            if (configuration != null) {
                return configuration;
            }

            ConfigFilesProvider configFilesProvider = () -> List.of(
                    Paths.get(getCore().getArgs().configSource())
            );

            ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
            Environment environment = new ImmutableEnvironment(IConstants.FILEPATH_CONFIG_ROOT);

            ConfigurationProvider provider = new ConfigurationProviderBuilder()
                    .withConfigurationSource(source)
                    .withEnvironment(environment)
                    .build();

            configuration = provider.bind(IConstants.CONFIG_APP_PREFIX, AppConfiguration.class);
            return configuration;
        }
    }
}
