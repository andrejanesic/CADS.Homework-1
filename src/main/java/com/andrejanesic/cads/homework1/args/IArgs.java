package com.andrejanesic.cads.homework1.args;

import com.andrejanesic.cads.homework1.constants.IConstants;
import com.andrejanesic.cads.homework1.core.IComponent;
import com.andrejanesic.cads.homework1.core.ThreadedComponent;
import com.andrejanesic.cads.homework1.core.exceptions.ArgsException;

/**
 * Wrapper for program argument parsing.
 */
public abstract class IArgs extends IComponent {

    /**
     * Parses the command-line arguments.
     *
     * @param args Arguments passed to main function.
     * @throws ArgsException Throws exception if a required argument is missing, or an error is encountered during
     *                       parsing.
     */
    public abstract void parse(String[] args) throws ArgsException;

    /**
     * App properties file path.
     *
     * @return App properties file path.
     */
    public String configSource() {
        return IConstants.DEFAULT_FILEPATH_APP_PROPERTIES;
    }
}
