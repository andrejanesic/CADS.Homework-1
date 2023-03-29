package com.andrejanesic.cads.homework1.config.cfg4j;

import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.config.IConfigLoader;
import com.andrejanesic.cads.homework1.constants.IConstants;
import com.andrejanesic.cads.homework1.core.exceptions.ConfigException;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.reload.ReloadStrategy;
import org.cfg4j.source.reload.strategy.ImmediateReloadStrategy;

import java.nio.file.Paths;
import java.util.List;

public class CFG4JLoader extends IConfigLoader {

    private AppConfiguration configuration;

    @Override
    public AppConfiguration load() throws ConfigException {
        if (configuration != null) {
            return configuration;
        }

        try {
            ConfigFilesProvider configFilesProvider = () -> List.of(
                    Paths.get(getCore().getArgs().configSource())
            );

            ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
            Environment environment = new ImmutableEnvironment(IConstants.FILEPATH_CONFIG_ROOT);
            ReloadStrategy reload = new ImmediateReloadStrategy();

            ConfigurationProvider provider = new ConfigurationProviderBuilder()
                    .withConfigurationSource(source)
                    .withEnvironment(environment)
                    .withReloadStrategy(reload)
                    .build();

            configuration = provider.bind(IConstants.CONFIG_APP_PREFIX, AppConfiguration.class);
        } catch (RuntimeException e) {
            throw new ConfigException(e.getMessage());
        }
        return configuration;
    }
}
