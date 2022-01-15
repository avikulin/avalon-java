package service.grammar;

import contracts.businesslogic.analyzers.GrammarAnalyzer;
import contracts.dataobjects.Expression;
import contracts.dataobjects.Token;

import java.util.List;

public class EmptyGrammarParser implements GrammarAnalyzer {
    @Override
    public Expression parseTokenSequence(List<Token> source, int sequenceId) {
        return null;
    }

    @Override
    public Expression parseScalarToken(Token token, int sequenceId) {
        return null;
    }
}
