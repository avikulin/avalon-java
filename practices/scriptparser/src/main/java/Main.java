import contracts.businesslogic.analyzers.GrammarAnalyzer;
import contracts.businesslogic.fabrics.*;
import contracts.businesslogic.manager.ScriptRunner;
import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.dal.ArgRepo;
import contracts.dal.SourceRepo;
import enums.CommandType;
import fabrics.*;
import repository.CmdLineArgumentRepo;
import repository.ScriptFileRepo;
import service.grammar.EmptyGrammarParser;
import service.grammar.ExpressionGrammarParser;
import service.grammar.SequenceGrammarParser;
import service.interpreter.ExpressionInterpreterImpl;
import service.interpreter.commands.*;
import service.lexer.LexicalParser;
import service.lexer.tokenizer.EmptyTokenizer;
import service.lexer.tokenizer.ExpressionTokenizer;
import service.lexer.tokenizer.SequenceTokenizer;
import service.manager.ScriptManager;
import utils.Tracer;

import java.io.*;

import static constants.Constants.*;
import static utils.ConsoleHelper.printToConsole;

public class Main {
    public static void main(String[] args) {


        TokenFabric tokenFabric = new TokenFabricImpl();
        TokenizerFabric tokenizerFabric = new TokenizerFabricImpl();
        Tokenizer expressionTokenizer = new ExpressionTokenizer(tokenFabric);
        Tokenizer sequenceTokenizer = new SequenceTokenizer(tokenFabric);
        Tokenizer emptyTokenizer = new EmptyTokenizer();

        tokenizerFabric.registerTokenizer(CommandType.SET, expressionTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.PRINT, sequenceTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.INPUT, sequenceTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.TRACE, emptyTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.HELP, emptyTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.QUITE, emptyTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.COMMENT, emptyTokenizer);

        ExpressionFabric expressionFabric = new ExpressionFabricImpl();
        GrammarAnalyzer expressionGrammar = new ExpressionGrammarParser(expressionFabric);
        GrammarAnalyzer sequenceGrammar = new SequenceGrammarParser(expressionFabric);
        GrammarAnalyzer emptyGrammar = new EmptyGrammarParser();

        GrammarFabric grammarFabric = new GrammarFabricImpl();
        grammarFabric.registerGrammar(CommandType.SET, expressionGrammar);
        grammarFabric.registerGrammar(CommandType.PRINT, sequenceGrammar);
        grammarFabric.registerGrammar(CommandType.INPUT, sequenceGrammar);
        grammarFabric.registerGrammar(CommandType.TRACE, emptyGrammar);
        grammarFabric.registerGrammar(CommandType.HELP, emptyGrammar);
        grammarFabric.registerGrammar(CommandType.QUITE, emptyGrammar);
        grammarFabric.registerGrammar(CommandType.COMMENT, emptyGrammar);

        LexicalParser lexicalParser = new LexicalParser(tokenizerFabric, grammarFabric);

        InterpreterFabric interpreterFabric = new InterpreterFabricImpl(new ExpressionInterpreterImpl());

        interpreterFabric.registerCommandInterpreter(CommandType.SET, new SetCmdInterpreterImpl());
        interpreterFabric.registerCommandInterpreter(CommandType.PRINT, new PrintCmdInterpreterImpl());
        interpreterFabric.registerCommandInterpreter(CommandType.INPUT, new InputCmdInterpreter());
        interpreterFabric.registerCommandInterpreter(CommandType.TRACE, new TraceCmdInterpreterImp());
        interpreterFabric.registerCommandInterpreter(CommandType.HELP, new HelpCommandInterpreterImpl());
        interpreterFabric.registerCommandInterpreter(CommandType.QUITE, new QuiteCommandInterpreter());
        interpreterFabric.registerCommandInterpreter(CommandType.COMMENT, new CommentCmdInterpreter());

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

        ScriptRunner runner = new ScriptManager(interpreterFabric, lexicalParser, in, out);

        ArgRepo argRepo = CmdLineArgumentRepo.get();

        if (args.length > 0){
            printToConsole(out, "\nCommand-line parameters received: ".concat(String.join(" ",args)));
            printToConsole(out, "\n");

            String arg1 = args[0];
            int paramCount = args.length;
            String paramKey = CMD_ARG_KEY_FILENAME+SEPARATOR_EQUALITY_SYMBOL; //"filename="
            int idx = arg1.indexOf(paramKey);
            int startIdxToFetchInRepo = 0;
            if (idx != -1){
                //String fileName = "src\\main\\java\\Script.avl";
                startIdxToFetchInRepo = 1;
                String fileName = arg1.substring(paramKey.length());
                fileName = fileName.replace("\\", "\\\\");
                File file = new File(fileName);
                SourceRepo sourceRepo = new ScriptFileRepo();
                try {
                    sourceRepo.loadFile(file);
                    runner.registerSourceRepo(sourceRepo);

                }catch (IllegalArgumentException e){
                    String msg = String.format("! Incorrect command-line param passed: %s\n", arg1);
                    printToConsole(out, msg);
                }
            }

            for(int i=startIdxToFetchInRepo; i<paramCount; i++){
                argRepo.putValue(args[i]);
            }
        }

        runner.run();
    }
}
