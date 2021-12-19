package service.grammar;

import contracts.businesslogic.analyzers.GrammarAnalyzer;
import contracts.businesslogic.fabrics.ExpressionFabric;
import contracts.dataobjects.Expression;
import contracts.dataobjects.Token;
import enums.TokenType;

import java.util.List;

public class SequenceGrammarParser implements GrammarAnalyzer {
    private final ExpressionFabric exprNodeFabric;

    public SequenceGrammarParser(ExpressionFabric expressionFabric) {
        if (expressionFabric == null) {
            throw new NullPointerException("Expression fabric object param must be not null");
        }
        this.exprNodeFabric = expressionFabric;
    }

    @Override
    public Expression parseTokenSequence(final List<Token> source, int sequenceId) {
        throw new UnsupportedOperationException("This grammar doesnt support sequences of tokens");
    }

    @Override
    public Expression parseScalarToken(Token token, int sequenceId) {
        TokenType type = token.getType();
        String source = token.getSource();
        switch (type) {
            case STRING_LITERAL: {
                if (source.length() > 2) {
                    return exprNodeFabric.createLiteralNode(source);
                }
                // не создаем пустые литеральные узлы, чтобы их не нужно было обрабатывать: ""
            }
            case VARIABLE:
                return exprNodeFabric.createVariableNode(source.trim());
        }
        throw new IllegalArgumentException("Wrong formatting of the print command");
    }
}
