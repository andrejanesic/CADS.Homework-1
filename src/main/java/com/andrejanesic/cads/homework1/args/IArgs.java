package com.andrejanesic.cads.homework1.args;

import com.andrejanesic.cads.homework1.constants.IConstants;
import com.andrejanesic.cads.homework1.core.IComponent;

/**
 * Wrapper for program argument parsing.
 */
public abstract class IArgs extends IComponent {

    /**
     * App properties file path.
     * @return App properties file path.
     */
    public String configSource() {
        return IConstants.DEFAULT_FILEPATH_APP_PROPERTIES;
    }
}
