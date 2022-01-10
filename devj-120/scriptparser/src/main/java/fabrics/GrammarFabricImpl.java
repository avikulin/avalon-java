package fabrics;

import contracts.businesslogic.analyzers.GrammarAnalyzer;
import enums.CommandType;

import java.util.HashMap;

public class GrammarFabricImpl implements contracts.businesslogic.fabrics.GrammarFabric {
    private HashMap<CommandType, GrammarAnalyzer> grammarParsers;

    public GrammarFabricImpl() {
        grammarParsers = new HashMap<>();
    }

    @Override
    public void registerGrammar(CommandType type, GrammarAnalyzer grammar){
        if (type == null){
            throw new NullPointerException("Command type param reference must be set");
        }

        if (grammar == null){
            throw new NullPointerException("Grammar analyzer object reference must be set");
        }
        grammarParsers.put(type, grammar);
    }

    @Override
    public GrammarAnalyzer getGrammarForType(CommandType type){
        if (type == null){
            throw new NullPointerException("Command type param reference must be set");
        }
        GrammarAnalyzer grammar = grammarParsers.get(type);
        if(grammar==null){
            throw new IllegalStateException("Try to access unregistered grammar");
        }
        return grammar;
    }
}
