package com.andrejanesic.cads.homework1.config;

import com.andrejanesic.cads.homework1.core.IComponent;
import com.andrejanesic.cads.homework1.core.ThreadedComponent;
import com.andrejanesic.cads.homework1.core.exceptions.ConfigException;
import org.cfg4j.source.files.FilesConfigurationSource;

/**
 * Loads the application configuration.
 */
public abstract class IConfig extends IComponent {

    /**
     * Loads the application configuration with a CFG4J
     * {@link FilesConfigurationSource} source.
     *
     * @return {@link AppConfiguration} with values.
     */
    public abstract AppConfiguration load() throws ConfigException;

    /**
     * Returns the loaded configuration, or null if not loaded.
     *
     * @return Application configuration (may return null.)
     */
    public abstract AppConfiguration getConfig();
}
