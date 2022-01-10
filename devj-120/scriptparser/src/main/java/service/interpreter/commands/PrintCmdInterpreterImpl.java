package service.interpreter.commands;

import contracts.dataobjects.Expression;
import contracts.dataobjects.Statement;
import enums.ExpressionNodeType;
import service.interpreter.commands.base.BaseCmdInterpreterImpl;

import java.text.DecimalFormat;

import static utils.ConsoleHelper.printToConsole;

public class PrintCmdInterpreterImpl extends BaseCmdInterpreterImpl {
    public PrintCmdInterpreterImpl() {
        super();
    }

    @Override
    public void interpretCommand(Statement cmd) {
        super.interpretCommand(cmd);

        if (getInterpreterContext() == null || getOutputContext() == null) {
            String msg = "Interpreter was improperly prepared";
            getLoggingContext().logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }

        getLoggingContext().logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());

        try {
            for (Expression expression : cmd) {
                ExpressionNodeType type = expression.getType();
                switch (type) {
                    case STRING_LITERAL: {
                        String value = expression.getStringValue();
                        printToConsole(getOutputContext(), value);
                        getLoggingContext().logInfo(this.getClass(), "\tliteral successfully printed: ", value);
                        break;
                    }
                    case VARIABLE: {
                        String variableName = expression.getStringValue();
                        Double variableValue = getInterpreterContext().interpretExpression(expression);
                        String outputStr = new DecimalFormat("0.0###").format(variableValue);
                        printToConsole(getOutputContext(), outputStr);

                        getLoggingContext().logInfo(this.getClass(), "\tvariable", variableName,
                                "successfully printed: ", outputStr);
                        break;
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException ex) {
            getLoggingContext().logError(this.getClass(), ex, "Unexpected exception rise");
            throw ex;
        }
        printToConsole(getOutputContext(), System.lineSeparator());
        getLoggingContext().logInfo(this.getClass(), "Command #", Integer.toString(cmd.getId()), " is done");
    }
}
