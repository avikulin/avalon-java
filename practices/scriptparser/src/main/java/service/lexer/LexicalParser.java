package service.lexer;

import contracts.businesslogic.DiagnosticLogger;
import contracts.businesslogic.analyzers.GrammarAnalyzer;
import contracts.businesslogic.analyzers.LexicalAnalyzer;
import contracts.businesslogic.fabrics.GrammarFabric;
import contracts.businesslogic.fabrics.TokenizerFabric;
import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.dataobjects.Expression;
import contracts.dataobjects.Statement;
import contracts.dataobjects.Token;
import dto.Command;
import enums.CommandType;
import utils.Tracer;

import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static constants.Constants.RESULT_NOT_FOUND;
import static constants.Constants.SEPARATOR_EQUALITY_SYMBOL;

public class LexicalParser implements LexicalAnalyzer {
    private final GrammarFabric grammars;
    private final TokenizerFabric tokenizers;

    public LexicalParser(TokenizerFabric tokenizes, GrammarFabric grammars) {
        this.tokenizers = tokenizes;
        this.grammars = grammars;
    }

    private CommandType detectClass(String cmdToken) {
        if (cmdToken == null || cmdToken.isEmpty()) {
            throw new IllegalArgumentException("Source text param must be set");
        }
        return CommandType.fromString(cmdToken);
    }

    @Override
    public Statement analyze(int cmdId, String sourceText) {
        if (sourceText == null || sourceText.isEmpty()) {
            throw new IllegalArgumentException("Source text param must be set");
        }
        DiagnosticLogger logger = Tracer.get();
        logger.logInfo(this.getClass(),"Analyze command #", Integer.toString(cmdId),":", sourceText);

        StringTokenizer literalTokenizer = new StringTokenizer(sourceText);
        String cmdToken = literalTokenizer.nextToken();
        String valueToken = sourceText.substring(cmdToken.length()).trim();

        CommandType type = detectClass(cmdToken);
        logger.logInfo(this.getClass(), "\tType detected:", type.toString());

        GrammarAnalyzer grammar = grammars.getGrammarForType(type);
        Tokenizer tokenizer = tokenizers.getTokenizerForType(type);

        switch (type) {
            case SET: {
                int separationPos = valueToken.indexOf(SEPARATOR_EQUALITY_SYMBOL);
                if (separationPos == RESULT_NOT_FOUND) {
                    String msg = "Wrong format of the SET command: symbol '=' is absent";
                    logger.logError(this.getClass(),msg);
                    throw new IllegalArgumentException(msg);
                }

                String lvalue = valueToken.substring(0, separationPos).trim();
                logger.logInfo(this.getClass(), "\tlvalue detected:", lvalue);

                String rvalue = valueToken.substring(separationPos + 1).trim();
                logger.logInfo(this.getClass(), "\trvalue detected:", rvalue);

                List<Token> tokens = tokenizer.tokenize(rvalue);
                logger.logInfo(this.getClass(), "\tcommand tokenized:",
                        tokens.stream().map(Token::toString).collect(Collectors.joining()));

                Expression expression = grammar.parseTokenSequence(tokens, cmdId);
                logger.logInfo(this.getClass(), "\texpression parsed:", expression.toString());

                Statement cmd = new Command(cmdId, type, lvalue);
                cmd.addRvalue(expression);

                logger.logInfo(this.getClass(), "\tcommand compiled:", cmd.toString());

                return cmd;
            }
            case PRINT: {
                logger.logInfo(this.getClass(), "\tparams detected:", valueToken);

                List<Token> tokens = tokenizer.tokenize(valueToken);
                logger.logInfo(this.getClass(), "\tcommand tokenized:",
                        tokens.stream().map(Token::toString).collect(Collectors.joining()));

                Statement cmd = new Command(cmdId, type, null);
                for (Token token : tokens) {
                    cmd.addRvalue(grammar.parseScalarToken(token, cmdId));
                }
                logger.logInfo(this.getClass(), "\tcommand compiled:", cmd.toString());
                return cmd;
            }

            case INPUT: {
                logger.logInfo(this.getClass(), "\tparams detected:", valueToken);

                List<Token> tokens = tokenizer.tokenize(valueToken);
                logger.logInfo(this.getClass(), "\tcommand tokenized:",
                        tokens.stream().map(Token::toString).collect(Collectors.joining()));

                if (tokens.size() > 2) {
                    String msg = "Wrong format of the INPUT command: only 2 params should be pass";
                    logger.logError(this.getClass(),msg);
                    throw new IllegalArgumentException(msg);
                }

                Statement cmd = new Command(cmdId, type, null);
                for (Token token : tokens) {
                    cmd.addRvalue(grammar.parseScalarToken(token, cmdId));
                }
                logger.logInfo(this.getClass(), "\tcommand compiled:", cmd.toString());
                return cmd;
            }

            case TRACE: {
                Statement cmd = new Command(cmdId, type, null);
                logger.logInfo(this.getClass(), "\tcommand compiled:", cmd.toString());
                return cmd;
            }

            default: {
                String msg = "Unknown command type";
                logger.logError(this.getClass(),msg);
                throw new UnsupportedOperationException(msg);
            }
        }
    }
}
