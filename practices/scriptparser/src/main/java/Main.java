import contracts.businesslogic.DiagnosticLogger;
import contracts.businesslogic.analyzers.GrammarAnalyzer;
import contracts.businesslogic.fabrics.ExpressionFabric;
import contracts.businesslogic.fabrics.GrammarFabric;
import contracts.businesslogic.fabrics.TokenFabric;
import contracts.businesslogic.fabrics.TokenizerFabric;
import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.dataobjects.Expression;
import contracts.dataobjects.Statement;
import enums.CommandType;
import fabrics.ExpressionFabricImpl;
import fabrics.GrammarFabricImpl;
import fabrics.TokenFabricImpl;
import fabrics.TokenizerFabricImpl;
import service.grammar.ExpressionGrammarParser;
import service.grammar.SequenceGrammarParser;
import service.lexer.LexicalParser;
import service.lexer.tokenizer.ExpressionTokenizer;
import service.lexer.tokenizer.SequenceTokenizer;
import utils.Tracer;

public class Main {
    public static void main(String[] args) {


        TokenFabric tokenFabric = new TokenFabricImpl();
        TokenizerFabric tokenizerFabric = new TokenizerFabricImpl();
        Tokenizer expressionTokenizer = new ExpressionTokenizer(tokenFabric);
        Tokenizer sequenceTokenizer = new SequenceTokenizer(tokenFabric);

        tokenizerFabric.registerTokenizer(CommandType.SET, expressionTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.PRINT, sequenceTokenizer);
        tokenizerFabric.registerTokenizer(CommandType.INPUT, sequenceTokenizer);

        ExpressionFabric expressionFabric = new ExpressionFabricImpl();
        GrammarAnalyzer expressionGrammar = new ExpressionGrammarParser(expressionFabric);
        GrammarAnalyzer sequenceGrammar = new SequenceGrammarParser(expressionFabric);

        GrammarFabric grammarFabric = new GrammarFabricImpl();
        grammarFabric.registerGrammar(CommandType.SET, expressionGrammar);
        grammarFabric.registerGrammar(CommandType.PRINT, sequenceGrammar);
        grammarFabric.registerGrammar(CommandType.INPUT, sequenceGrammar);

        LexicalParser lexicalParser = new LexicalParser(tokenizerFabric, grammarFabric);

        String s1 = "set $t = $x * 21 + 13 * tan ( $y + 11 ) / 15 - 14 * $z / 11 - log($y + 11)";
        Statement statementA = lexicalParser.analyze(1, s1);
        System.out.println(statementA);

        String s2 = "print \"$sum = \", $n1, \"+\", $n2, \"-42 = \", $sum";
        Statement statementB = lexicalParser.analyze(2, s2);
        System.out.println(statementB);


        String s3 = "input \"What is your age: \", $age";
        Statement statementC = lexicalParser.analyze(3, s3);
        System.out.println(statementC);

        DiagnosticLogger logger = Tracer.get();
        System.out.println("\n trace info:");
        System.out.println(logger.getLog());
    }
}
