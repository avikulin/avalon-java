package service.interpreter.commands;

import contracts.dataobjects.Statement;
import enums.FunctionType;
import enums.OperationType;
import enums.TokenType;
import service.interpreter.commands.base.BaseCmdInterpreterImpl;

import static utils.ConsoleHelper.printToConsole;

public class HelpCommandInterpreterImpl extends BaseCmdInterpreterImpl {
    public HelpCommandInterpreterImpl() {
        super();
    }

    @Override
    public void interpretCommand(Statement cmd) {
        super.interpretCommand(cmd);

        getLoggingContext().logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());

        printToConsole(getOutputContext(), "USER HELP INFORMATION\n");
        printToConsole(getOutputContext(), "---------------------\n");
        printToConsole(getOutputContext(), System.lineSeparator());
        printToConsole(getOutputContext(), TokenType.getDescription());
        printToConsole(getOutputContext(), System.lineSeparator());
        printToConsole(getOutputContext(), OperationType.getDescription());
        printToConsole(getOutputContext(), System.lineSeparator());
        printToConsole(getOutputContext(), FunctionType.getDescription());
        printToConsole(getOutputContext(), System.lineSeparator());
        printToConsole(getOutputContext(), "---");
        printToConsole(getOutputContext(), System.lineSeparator());

        getLoggingContext().logInfo(this.getClass(), "Command #", Integer.toString(cmd.getId()), " is done");
    }
}
