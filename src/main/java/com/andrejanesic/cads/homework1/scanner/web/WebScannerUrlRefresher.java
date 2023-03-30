package com.andrejanesic.cads.homework1.scanner.web;

import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.UnexpectedRuntimeComponentException;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Refreshes {@link WebScanner}'s cache of URLs.
 */
@RequiredArgsConstructor
public class WebScannerUrlRefresher extends LoopRunnable {

    private long lastRefresh = System.currentTimeMillis();

    @NonNull
    private final Map index;
    @NonNull
    private final long refreshSleep;

    @Override
    public void loop() throws ComponentException {
        // TODO check if urls refreshed as a group, or each individually after timeout
        try {
            Thread.sleep(refreshSleep);
        } catch (InterruptedException e) {
            ;
        }
        long curr = System.currentTimeMillis();
        if (curr > lastRefresh) {
            lastRefresh = curr;
            index.clear();
        }
    }
}
