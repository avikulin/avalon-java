package service.interpreter.commands.base;

import contracts.businesslogic.utils.DiagnosticLogger;
import contracts.businesslogic.interpreters.CommandInterpreter;
import contracts.businesslogic.interpreters.ExpressionInterpreter;
import contracts.dataobjects.Statement;
import utils.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;

public abstract class BaseCmdInterpreterImpl implements CommandInterpreter {
    private ExpressionInterpreter interpreterContext;
    private Map<String, Double> variableContext;
    private DiagnosticLogger logger;
    private BufferedWriter outputContext;
    private BufferedReader inputContext;

    public BaseCmdInterpreterImpl() {
        logger = Tracer.get();
    }

    @Override
    public void registerVariableContext(Map<String, Double> variableContext) {
        if (variableContext == null) {
            throw new NullPointerException("Variable context object param must be not null");
        }
        this.variableContext = variableContext;
    }

    @Override
    public void registerInterpreterContext(ExpressionInterpreter interpreterContext) {
        if (interpreterContext == null) {
            throw new NullPointerException("Interpreter context object param must be not null");
        }
        this.interpreterContext = interpreterContext;
    }
    @Override
    public void registerInputContext(BufferedReader inputContext) {
        if (inputContext == null) {
            throw new NullPointerException("Input context object param must be not null");
        }
        this.inputContext = inputContext;
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
        if (cmd == null){
            String msg = "Statement object param must be not-null";
            logger.logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public ExpressionInterpreter getInterpreterContext() throws IllegalStateException {
        return interpreterContext;
    }

    @Override
    public Map<String, Double> getVariableContext() throws IllegalStateException {
        return variableContext;
    }

    @Override
    public BufferedReader getInputContext() throws IllegalStateException {
        return inputContext;
    }

    @Override
    public BufferedWriter getOutputContext() throws IllegalStateException {
        return outputContext;
    }

    @Override
    public DiagnosticLogger getLoggingContext() {
        return Tracer.get();
    }
}
