package service.interpreter;

import contracts.dataobjects.Expression;
import contracts.dataobjects.Statement;

import java.io.*;

public class SetCmdInterpreterImpl extends BaseCmdInterpreterImpl {

    public SetCmdInterpreterImpl() {
        super();
    }

    @Override
    public void registerInputContext(BufferedReader inputContext) {
        throw new UnsupportedOperationException("Command SET doesn't support user input operations");
    }

    @Override
    public void registerOutputContext(BufferedWriter outputContext) {
        throw new UnsupportedOperationException("Command SET doesn't support user output operations");
    }

    @Override
    public void interpretCommand(Statement cmd) {
        if (variableContext == null || interpreterContext == null) {
            String msg = "Interpreter was improperly prepared";
            logger.logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }
        logger.logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());

        Expression expression = cmd.getRValue(0);
        String variableToSet = cmd.getLValue();
        logger.logInfo(this.getClass(), "\tvariable detected: ", variableToSet);
        try {
            double variableValue = interpreterContext.interpretExpression(expression);
            logger.logInfo(this.getClass(), "\tvariable value calculated: ", variableToSet);
            variableContext.put(variableToSet, variableValue);
            logger.logInfo(this.getClass(), "\tvariable value successfully saved in the context");
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException ex) {
            StringWriter stackTraceContent = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stackTraceContent);
            ex.printStackTrace(printWriter);
            logger.logError(this.getClass(), "Unexpected exception rise: ", stackTraceContent.toString());
        }
    }
}
