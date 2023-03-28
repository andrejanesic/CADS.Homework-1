package com.andrejanesic.cads.homework1.config;

import org.cfg4j.source.files.FilesConfigurationSource;

/**
 * Loads the application configuration.
 */
public interface IConfigLoader {
    /**
     * Loads the application configuration with a CFG4J
     * {@link FilesConfigurationSource} source.
     *
     * @return {@link AppConfiguration} with values.
     */
    AppConfiguration load();
}
