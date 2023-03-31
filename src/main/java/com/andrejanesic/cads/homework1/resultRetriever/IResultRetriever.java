package com.andrejanesic.cads.homework1.resultRetriever;

import com.andrejanesic.cads.homework1.core.ThreadPoolThreadedComponent;
import com.andrejanesic.cads.homework1.job.result.Result;

import java.util.concurrent.Future;

public abstract class IResultRetriever extends
        ThreadPoolThreadedComponent<Future<Result>> {
}
