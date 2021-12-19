package contracts.businesslogic.interpreters;

import contracts.dataobjects.Statement;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface CommandInterpreter {
    void registerInterpreterContext(ExpressionInterpreter interpreterContext);

    void registerInputContext(BufferedReader inputContext);

    void registerOutputContext(BufferedWriter outputContext);

    void interpretCommand(Statement cmd);
}
