package service.interpreter.commands;

import contracts.businesslogic.interpreters.ExpressionInterpreter;
import contracts.dataobjects.Statement;
import service.interpreter.commands.base.BaseCmdInterpreterImpl;

import java.io.BufferedReader;
import java.util.Map;

import static utils.ConsoleHelper.printToConsole;

public class TraceCmdInterpreterImp extends BaseCmdInterpreterImpl {
    public TraceCmdInterpreterImp() {
        super();
    }

    @Override
    public void interpretCommand(Statement cmd) {
        super.interpretCommand(cmd);

        if (getOutputContext() == null) {
            String msg = "Interpreter was improperly prepared";
            getLoggingContext().logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }

        getLoggingContext().logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());

        printToConsole(getOutputContext(), "Diagnostic log:\n");
        printToConsole(getOutputContext(), getLoggingContext().getLog());
        printToConsole(getOutputContext(), "\n\n");
    }
}
