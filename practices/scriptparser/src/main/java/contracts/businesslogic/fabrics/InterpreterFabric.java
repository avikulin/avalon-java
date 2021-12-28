package contracts.businesslogic.fabrics;

import contracts.businesslogic.interpreters.CommandInterpreter;
import enums.CommandType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;

public interface InterpreterFabric {
    void registerCommandInterpreter(CommandType type, CommandInterpreter interpreter);

    CommandInterpreter getInterpreterForType(CommandType type);

    void bindToContext(BufferedReader readerCtx, BufferedWriter writerCtx, Map<String, Double> variableCtx);
}
