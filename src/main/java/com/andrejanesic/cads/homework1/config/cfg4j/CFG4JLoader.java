package com.andrejanesic.cads.homework1.config.cfg4j;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.config.AppConfiguration;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.constants.IConstants;
import com.andrejanesic.cads.homework1.core.exceptions.ConfigException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.compose.MergeConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.inmemory.InMemoryConfigurationSource;
import org.cfg4j.source.reload.ReloadStrategy;
import org.cfg4j.source.reload.strategy.ImmediateReloadStrategy;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CFG4JLoader extends IConfig {

    private final IArgs args;
    private final IExceptionHandler exceptionHandler;
    private AppConfiguration configuration;

    /**
     * @param args args interface
     * @deprecated use the new default constructor:
     * {@link #CFG4JLoader(IArgs, IExceptionHandler)}
     */
    public CFG4JLoader(IArgs args) {
        this(args, null);
    }

    /**
     * Default constructor
     *
     * @param args             args interface
     * @param exceptionHandler exception handler
     */
    @Inject
    public CFG4JLoader(IArgs args, IExceptionHandler exceptionHandler) {
        this.args = args;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public AppConfiguration load() throws ConfigException {
        if (configuration != null) {
            return configuration;
        }

        try {
            ConfigFilesProvider configFilesProvider = () -> List.of(
                    Paths.get(args.configSource())
            );

            Properties fallbackProperties = new Properties();
            fallbackProperties.setProperty("delimiter", "\\s+");
            ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
            ConfigurationSource fallback = new InMemoryConfigurationSource(fallbackProperties);
            ConfigurationSource merged = new MergeConfigurationSource(fallback, source);
            Environment environment = new ImmutableEnvironment(IConstants.FILEPATH_CONFIG_ROOT);
            ReloadStrategy reload = new ImmediateReloadStrategy();

            ConfigurationProvider provider = new ConfigurationProviderBuilder()
                    .withConfigurationSource(merged)
                    .withEnvironment(environment)
                    .withReloadStrategy(reload)
                    .build();

            configuration = provider.bind(IConstants.CONFIG_APP_PREFIX, AppConfiguration.class);

            // validate
            String delimiter = configuration.delimiter();
            if (delimiter == null || delimiter.length() == 0) {
                configuration = new AppConfiguration() {
                    @Override
                    public String delimiter() {
                        return "\\s+";
                    }

                    @Override
                    public String[] keywords() {
                        return configuration.keywords();
                    }

                    @Override
                    public String fileCorpusPrefix() {
                        return configuration.fileCorpusPrefix();
                    }

                    @Override
                    public int directoryCrawlerSleepTime() {
                        return configuration.directoryCrawlerSleepTime();
                    }

                    @Override
                    public long fileScanningSizeLimit() {
                        return configuration.fileScanningSizeLimit();
                    }

                    @Override
                    public int hopCount() {
                        return configuration.hopCount();
                    }

                    @Override
                    public int urlRefreshTime() {
                        return configuration.urlRefreshTime();
                    }
                };
            }
            try {
                //noinspection DataFlowIssue
                Pattern.compile(delimiter);
            } catch (PatternSyntaxException e) {
                ConfigException ex = new ConfigException(
                        "Delimiter \"" +
                                delimiter +
                                "\" is not a valid regular expression"
                );

                if (exceptionHandler == null)
                    throw ex;
                exceptionHandler.handle(ex);
            }

            String[] keywords = configuration.keywords();
            if (keywords == null || keywords.length == 0)
                throw new ConfigException("No keywords defined");
            for (String k : keywords) {
                if (k.split(delimiter).length > 1) {
                    ConfigException ex = new ConfigException(
                            "Keyword \"" + k +
                                    "\" contains delimiter: \"" +
                                    delimiter + "\"");

                    if (exceptionHandler == null)
                        throw ex;
                    exceptionHandler.handle(ex);
                }
            }

            int directoryCrawlerSleepTime = configuration.directoryCrawlerSleepTime();
            if (directoryCrawlerSleepTime < 0) {
                ConfigException ex = new ConfigException(
                        "directoryCrawlerSleepTime cannot be " +
                                "lower than 0");

                if (exceptionHandler == null)
                    throw ex;
                exceptionHandler.handle(ex);
            }

            long fileScanningSizeLimit = configuration.fileScanningSizeLimit();
            if (fileScanningSizeLimit < 0) {
                ConfigException ex = new ConfigException(
                        "fileScanningSizeLimit " +
                                "cannot be lower than 0");

                if (exceptionHandler == null)
                    throw ex;
                exceptionHandler.handle(ex);
            }

            int hopCount = configuration.hopCount();
            if (hopCount < 0) {
                ConfigException ex = new ConfigException("hopCount cannot be " +
                        "lower than 0");

                if (exceptionHandler == null)
                    throw ex;
                exceptionHandler.handle(ex);
            }

            int urlRefreshTime = configuration.urlRefreshTime();
            if (urlRefreshTime < 0) {
                ConfigException ex = new ConfigException("urlRefreshTime " +
                        "cannot be lower " +
                        "than 0");

                if (exceptionHandler == null)
                    throw ex;
                exceptionHandler.handle(ex);
            }

        } catch (RuntimeException e) {
            ConfigException ex = new ConfigException(e);

            if (exceptionHandler == null)
                throw ex;
            exceptionHandler.handle(ex);
        }
        return configuration;
    }

    @Override
    public AppConfiguration getConfig() {
        try {
            return load();
        } catch (ConfigException e) {
            if (exceptionHandler != null)
                exceptionHandler.handle(e);
            return null;
        }
    }

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {

    }

    @Override
    public void beforeEnd() {

    }
}
