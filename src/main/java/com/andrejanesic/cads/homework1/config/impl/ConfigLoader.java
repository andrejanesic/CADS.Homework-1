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

public class ConfigLoader implements IConfigLoader {

    private static final Object CONFIGURATION_LOCK = new Object();
    private static AppConfiguration CONFIGURATION;

    @Override
    public AppConfiguration load() {
        if (CONFIGURATION != null) {
            return CONFIGURATION;
        }

        synchronized (CONFIGURATION_LOCK) {
            if (CONFIGURATION != null) {
                return CONFIGURATION;
            }

            ConfigFilesProvider configFilesProvider = () -> List.of(
                    Paths.get(IConstants.FILEPATH_APP_PROPERTIES)
            );

            ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
            Environment environment = new ImmutableEnvironment(IConstants.FILEPATH_CONFIG_ROOT);

            ConfigurationProvider provider = new ConfigurationProviderBuilder()
                    .withConfigurationSource(source)
                    .withEnvironment(environment)
                    .build();

            CONFIGURATION = provider.bind("app", AppConfiguration.class);
            return CONFIGURATION;
        }
    }
}
