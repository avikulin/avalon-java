package service.interpreter.commands;

import contracts.dataobjects.Expression;
import contracts.dataobjects.Statement;
import service.interpreter.commands.base.BaseCmdInterpreterImpl;

public class SetCmdInterpreterImpl extends BaseCmdInterpreterImpl {

    public SetCmdInterpreterImpl() {
        super();
    }

    @Override
    public void interpretCommand(Statement cmd) {
        super.interpretCommand(cmd);

        if (getInterpreterContext() == null) {
            String msg = "Interpreter was improperly prepared";
            getLoggingContext().logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }

        getLoggingContext().logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());

        Expression expression = cmd.getRValue(0);
        String variableToSet = cmd.getLValue();
        getLoggingContext().logInfo(this.getClass(), "\tvariable detected: ", variableToSet);
        try {
            double variableValue = getInterpreterContext().interpretExpression(expression);
            getLoggingContext().logInfo(this.getClass(), "\tvariable value calculated: ", variableToSet);
            getVariableContext().put(variableToSet, variableValue);
            getLoggingContext().logInfo(this.getClass(), "\tvariable value successfully saved in the context");
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException ex) {
            getLoggingContext().logError(this.getClass(), ex, "Unexpected exception rise");
        }
    }
}
