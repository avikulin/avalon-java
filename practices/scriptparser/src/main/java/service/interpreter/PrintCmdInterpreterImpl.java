package service.interpreter;

import contracts.dataobjects.Expression;
import contracts.dataobjects.Statement;
import enums.NodeType;

import java.io.*;
import java.text.DecimalFormat;

public class PrintCmdInterpreterImpl extends BaseCmdInterpreterImpl {
    private BufferedWriter outputContext;

    public PrintCmdInterpreterImpl() {
        super();
    }

    @Override
    public void registerInputContext(BufferedReader inputContext) {
        throw new UnsupportedOperationException("Command PRINT doesn't support user input operations");
    }

    @Override
    public void registerOutputContext(BufferedWriter outputContext) {
        if (outputContext == null) {
            throw new NullPointerException("Output context object param must be not null");
        }
        this.outputContext = outputContext;
    }

    @Override
    public void interpretCommand(Statement cmd) {
        if (interpreterContext == null || outputContext == null) {
            String msg = "Interpreter was improperly prepared";
            logger.logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }
        logger.logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());

        try {
            for (Expression expression : cmd) {
                NodeType type = expression.getType();
                switch (type) {
                    case STRING_LITERAL: {
                        String value = expression.getStringValue();
                        outputContext.write(value);
                        logger.logInfo(this.getClass(), "\tliteral successfully printed: ", value);
                    }
                    case VARIABLE: {
                        String variableName = expression.getStringValue();
                        Double variableValue = interpreterContext.interpretExpression(expression);
                        String outputStr = new DecimalFormat("#.0#").format(variableValue);
                        outputContext.write(outputStr);
                        logger.logInfo(this.getClass(), "\tvariable", variableName,
                                "successfully printed: ", outputStr);
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalStateException | NullPointerException | IOException ex) {
            StringWriter stackTraceContent = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stackTraceContent);
            ex.printStackTrace(printWriter);
            logger.logError(this.getClass(), "Unexpected exception rise: ", stackTraceContent.toString());
        }

        logger.logInfo(this.getClass(), "Command #", Integer.toString(cmd.getId()), " is done");
    }
}
