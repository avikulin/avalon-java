package contracts.businesslogic.interpreters;

import contracts.businesslogic.utils.DiagnosticLogger;
import contracts.dataobjects.Statement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;

public interface CommandInterpreter {
    void registerInterpreterContext(ExpressionInterpreter interpreterContext) throws NullPointerException;

    void registerVariableContext(Map<String, Double> variableContext) throws NullPointerException;

    void registerInputContext(BufferedReader inputContext) throws NullPointerException;

    void registerOutputContext(BufferedWriter outputContext) throws NullPointerException;

    ExpressionInterpreter getInterpreterContext() throws IllegalStateException;

    Map<String, Double> getVariableContext() throws IllegalStateException;

    BufferedReader getInputContext() throws IllegalStateException;

    BufferedWriter getOutputContext() throws IllegalStateException;

    DiagnosticLogger getLoggingContext();

    void interpretCommand(Statement cmd);
}
