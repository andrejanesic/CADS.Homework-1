package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import com.andrejanesic.cads.homework1.resultRetriever.impl.ResultRetrieverCallable;

import java.util.regex.Pattern;

public class ClearRoutine implements IRoutine {

    private final ICLOutput iclOutput;
    private final IResultRetriever resultRetriever;
    private final boolean file;

    public ClearRoutine(
            ICLOutput iclOutput,
            IResultRetriever resultRetriever,
            boolean file
    ) {
        this.iclOutput = iclOutput;
        this.resultRetriever = resultRetriever;
        this.file = file;
    }


    @Override
    public void doRoutine() throws RoutineException {
        resultRetriever.invalidateStores(
                Query.builder()
                        .type(file ? JobType.FILE : JobType.WEB)
                        .uri(Pattern.compile("^.*$"))
                        .build()
        );
        iclOutput.warning(
                "Cleared caches"
        );
    }

    @Override
    public void undoRoutine() throws RoutineException {

    }
}
