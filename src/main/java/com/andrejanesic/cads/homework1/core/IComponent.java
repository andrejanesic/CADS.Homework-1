package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.Main;

/**
 * Base component class.
 */
public abstract class IComponent {

    /**
     * References {@link Core} for easier use by components and test mocking.
     * @return {@link Core} reference.
     */
    public Core getCore() {
        return Main.getCore();
    }
}
