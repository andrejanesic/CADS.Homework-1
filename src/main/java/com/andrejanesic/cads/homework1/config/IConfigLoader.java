package com.andrejanesic.cads.homework1.config;

import com.andrejanesic.cads.homework1.core.IComponent;
import org.cfg4j.source.files.FilesConfigurationSource;

/**
 * Loads the application configuration.
 */
public abstract class IConfigLoader extends IComponent {
    /**
     * Loads the application configuration with a CFG4J
     * {@link FilesConfigurationSource} source.
     *
     * @return {@link AppConfiguration} with values.
     */
    public abstract AppConfiguration load();
}
