package service.manager;

import contracts.businesslogic.fabrics.InterpreterFabric;
import contracts.businesslogic.interpreters.CommandInterpreter;
import contracts.businesslogic.manager.ScriptRunner;
import contracts.businesslogic.utils.DiagnosticLogger;
import contracts.dal.ArgRepo;
import contracts.dal.SourceRepo;
import contracts.dataobjects.Statement;
import repository.CmdLineArgumentRepo;
import service.lexer.LexicalParser;
import utils.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static utils.ConsoleHelper.printToConsole;
import static utils.ConsoleHelper.readFromConsole;

public class ScriptManager implements ScriptRunner {
    private final InterpreterFabric interpreterFabric;
    private final LexicalParser lexicalParser;
    private final Map<String, Double> variableContext;
    private final DiagnosticLogger logger;
    private final ArgRepo parameterContext;
    private final BufferedReader in;
    private final BufferedWriter out;
    private SourceRepo sourceContext;

    public ScriptManager(InterpreterFabric interpreterFabric, LexicalParser lexicalParser,
                         BufferedReader in, BufferedWriter out) {
        logger = Tracer.get();
        if (interpreterFabric == null) {
            String msg = "Interpreter fabric object reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new NullPointerException(msg);
        }
        if (lexicalParser == null) {
            String msg = "Lexical parser object reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new NullPointerException(msg);
        }

        if (in == null) {
            String msg = "Input stream reader object reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new NullPointerException(msg);

        }

        if (out == null) {
            String msg = "Output stream writer object reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new NullPointerException(msg);
        }

        this.interpreterFabric = interpreterFabric;
        this.lexicalParser = lexicalParser;
        this.in = in;
        this.out = out;
        this.variableContext = new HashMap<>();
        this.parameterContext = CmdLineArgumentRepo.get();
        this.interpreterFabric.bindToContext(this.in, this.out, this.variableContext);
    }

    @Override
    public void registerSourceRepo(SourceRepo sourceRepo) {
        if (sourceRepo == null) {
            String msg = "Script file reader object reference must be not null";
            logger.logError(this.getClass(), msg);
            throw new NullPointerException(msg);
        }
        this.sourceContext = sourceRepo;
    }

    private void processCommand(String s, int cmdId) {
        if (s != null && (!s.isEmpty())) {
            Statement cmd = lexicalParser.analyze(cmdId, s);
            CommandInterpreter interpreter = interpreterFabric.getInterpreterForType(cmd.getType());
            interpreter.interpretCommand(cmd);
        }
    }

    @Override
    public void run() {
        printToConsole(out, "\nWelcome to Avalon Script Interpreter (ASI)");
        printToConsole(out, System.lineSeparator());
        printToConsole(out, "------------------------------------------");
        printToConsole(out, System.lineSeparator());

        Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();
        printToConsole(out, "Current dir: " + path.toString());
        printToConsole(out, System.lineSeparator());

        int commandId = 0;
        if (sourceContext != null && sourceContext.isReady()) {
            printToConsole(out, "\nExecuting script from file: ".concat(sourceContext.getFileName()));
            printToConsole(out, "\n\n");
            try {
                for (String s : sourceContext) {
                    commandId = sourceContext.getReadPosition();
                    processCommand(s, commandId);
                }
            } catch (Exception e) {
                String msg = String.format("\n!Unexpected error at line #%d: %s",
                        sourceContext.getReadPosition(), e.getMessage());
                printToConsole(out, msg);
                logger.logError(this.getClass(), e, msg);
            }
        }

        printToConsole(out, "\nEntering interactive mode (type \"quite\" to exit)...\n\n");

        while (true) {
            try {
                printToConsole(out, "asi> ");
                String s = readFromConsole(in);
                commandId++;
                processCommand(s, commandId);
            } catch (Exception e) {
                String msg = String.format("Unexpected error: %s\n", e.getMessage());
                printToConsole(out, msg);
                logger.logError(this.getClass(), e, msg);
            }
        }
    }
}
