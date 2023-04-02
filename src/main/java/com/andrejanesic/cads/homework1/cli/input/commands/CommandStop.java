package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.cli.input.ICLInput;
import com.andrejanesic.cads.homework1.core.exceptions.CLInputException;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.core.routines.IRoutine;
import com.andrejanesic.cads.homework1.core.routines.RoutineManager;
import com.andrejanesic.cads.homework1.core.routines.StopRoutine;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CommandStop extends ICommand {

    private static final String SYNTAX_ERROR = "Invalid syntax: stop";

    @Getter
    @Setter
    private ICLInput iclInput;

    @Inject
    public CommandStop() {
        super();
        command("stop", "exit");
    }

    @Override
    public void exec() throws CLInputException {
        List<String> args = getCommandLine().getArgList();

        if (args.size() != 1) {
            throw new CLInputException(SYNTAX_ERROR);
        }
        IRoutine routine = new StopRoutine();
        RoutineManager rm = RoutineManager.getInstance();
        try {
            rm.addRoutine(routine);
            if (iclInput != null) {
                synchronized (iclInput.getKeepAliveLock()) {
                    try {
                        iclInput.getKeepAliveLock().wait();
                    } catch (InterruptedException e) {
                        ;
                    }
                }
            }
        } catch (RoutineException e) {
            throw new CLInputException(e);
        }
    }
}
