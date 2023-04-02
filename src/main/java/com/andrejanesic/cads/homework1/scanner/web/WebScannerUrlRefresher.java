package com.andrejanesic.cads.homework1.scanner.web;

import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import com.andrejanesic.cads.homework1.resultRetriever.impl.ResultRetriever;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Refreshes {@link WebScanner}'s cache of URLs.
 */
@RequiredArgsConstructor
public class WebScannerUrlRefresher extends LoopRunnable {

    private final IResultRetriever resultRetriever;
    @NonNull
    private final Map[] indexes;
    private final long refreshSleep;
    private long lastRefresh = System.currentTimeMillis();

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
            for (Map index : indexes) {
                index.clear();
            }
        }
    }
}
