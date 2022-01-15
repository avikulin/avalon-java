package contracts.businesslogic.manager;

import contracts.businesslogic.fabrics.InterpreterFabric;
import contracts.businesslogic.interpreters.CommandInterpreter;
import contracts.dal.SourceRepo;
import service.lexer.LexicalParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;

public interface ScriptRunner {
    void registerSourceRepo(SourceRepo sourceRepo);
    void run();
}
