package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.args.IArgs;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Binds all app components together.
 */
@AllArgsConstructor
public class Core {

    @Getter
    private IArgs args;
}
