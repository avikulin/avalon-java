package service.interpreter.commands;

import contracts.dataobjects.Statement;
import service.interpreter.commands.base.BaseCmdInterpreterImpl;

import static utils.ConsoleHelper.printToConsole;
import static utils.ConsoleHelper.readFromConsole;

public class InputCmdInterpreter extends BaseCmdInterpreterImpl {
    public InputCmdInterpreter() {
        super();
    }

    @Override
    public void interpretCommand(Statement cmd) {
        super.interpretCommand(cmd);

        if (getInterpreterContext() == null || getInputContext() == null || getOutputContext() == null) {
            String msg = "Interpreter was improperly prepared";
            getLoggingContext().logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }

        getLoggingContext().logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());

        String queryMsg = cmd.getRValue(0).getStringValue();
        String variableName = cmd.getRValue(1).getStringValue();

        while (true) {
            String userInput = "";
            try {
                printToConsole(getOutputContext(), queryMsg.concat(" : "));
                userInput = readFromConsole(getInputContext());
                Double variableValue = Double.parseDouble(userInput);
                getVariableContext().put(variableName, variableValue);
                getLoggingContext().logInfo(this.getClass(), "\tUser input successfully saved in variable", variableName);
                getLoggingContext().logInfo(this.getClass(), "Command #", Integer.toString(cmd.getId()), " is done");
                return;
            } catch (NumberFormatException e) {
                String msg = "You have entered invalid decimal value. Try again...\n";
                printToConsole(getOutputContext(), msg);
                getLoggingContext().logError(this.getClass(), e, "Invalid user input", " : ", userInput);
            }
        }
    }
}
