package fabrics;

import contracts.businesslogic.fabrics.InterpreterFabric;
import contracts.businesslogic.interpreters.CommandInterpreter;
import contracts.businesslogic.interpreters.ExpressionInterpreter;
import contracts.businesslogic.utils.DiagnosticLogger;
import enums.CommandType;
import utils.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;

public class InterpreterFabricImpl implements InterpreterFabric {
    private final Map<CommandType, CommandInterpreter> interpreterCollection;
    private final ExpressionInterpreter expressionInterpreter;
    private final DiagnosticLogger logger;
    private boolean isReady;

    public InterpreterFabricImpl(ExpressionInterpreter expressionInterpreter) {
        logger = Tracer.get();
        if (expressionInterpreter == null) {
            String msg = "Expression interpreter object param must be set";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        this.expressionInterpreter = expressionInterpreter;
        interpreterCollection = new HashMap<>();
        isReady = false;
    }

    @Override
    public void registerCommandInterpreter(CommandType type, CommandInterpreter interpreter) {
        if (type == null) {
            String msg = "Command type object param must be set";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        if (interpreter == null) {
            String msg = "Interpreter object param must be set";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        interpreterCollection.put(type, interpreter);
    }

    @Override
    public CommandInterpreter getInterpreterForType(CommandType type) {
        if(!isReady){
            String msg = "Object haven't been properly bind to context";
            logger.logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }

        if (type == null) {
            String msg = "Command type object param reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        CommandInterpreter interpreter = interpreterCollection.get(type);
        if (interpreter == null) {
            String msg = String.format("There is not registered interpreter for command type \"%s\"", type);
            logger.logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }
        return interpreter;
    }

    @Override
    public void bindToContext(BufferedReader readerCtx, BufferedWriter writerCtx, Map<String, Double> variableCtx) {

        if (readerCtx == null) {
            String msg = "Reader object param reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        if (writerCtx == null) {
            String msg = "Writer object param reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        if (variableCtx == null) {
            String msg = "Variable context object param reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        if (expressionInterpreter == null) {
            String msg = "Expression interpreter object param reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }

        expressionInterpreter.registerVariableContext(variableCtx);

        for (Map.Entry<CommandType, CommandInterpreter> entry : interpreterCollection.entrySet()) {
            CommandInterpreter interpreter = entry.getValue();
            interpreter.registerInputContext(readerCtx);
            interpreter.registerOutputContext(writerCtx);
            interpreter.registerVariableContext(variableCtx);
            interpreter.registerInterpreterContext(expressionInterpreter);
        }

        isReady = true;
    }
}
