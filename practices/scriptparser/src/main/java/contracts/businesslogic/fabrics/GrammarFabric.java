package contracts.businesslogic.fabrics;

import contracts.businesslogic.analyzers.GrammarAnalyzer;
import enums.CommandType;

public interface GrammarFabric {
    void registerGrammar(CommandType type, GrammarAnalyzer grammar);
    GrammarAnalyzer getGrammarForType(CommandType type);
}
