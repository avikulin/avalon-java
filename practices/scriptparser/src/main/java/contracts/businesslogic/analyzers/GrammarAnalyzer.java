package contracts.businesslogic.analyzers;

import contracts.dataobjects.Expression;
import contracts.dataobjects.Token;

import java.util.List;

public interface GrammarAnalyzer {
    Expression parseTokenSequence(final List<Token> source, int sequenceId);
    Expression parseScalarToken(Token token, int sequenceId);
}
