package service.interpreter.commands;

import contracts.dataobjects.Expression;
import contracts.dataobjects.Statement;
import contracts.dataobjects.Token;
import enums.ExpressionNodeType;
import enums.TokenType;
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

        try {
            int numberOfTokens = cmd.getRValueLength();
            Expression first = cmd.getRValue(0);
            Expression second = cmd.getRValue(1);
            if (numberOfTokens != 2 ||
                    first.getType() != ExpressionNodeType.STRING_LITERAL ||
                    second.getType() != ExpressionNodeType.VARIABLE ){
                throw new IllegalStateException();
            }
        } catch (IllegalStateException | NullPointerException | IndexOutOfBoundsException e) {
            String msg = "Incorrect operands formatting in command #" + cmd.getId();
            getLoggingContext().logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }
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
