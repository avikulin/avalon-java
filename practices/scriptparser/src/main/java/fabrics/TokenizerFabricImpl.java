package fabrics;

import contracts.businesslogic.fabrics.GrammarFabric;
import contracts.businesslogic.fabrics.TokenizerFabric;
import contracts.businesslogic.tokenizers.Tokenizer;
import enums.CommandType;

import java.util.HashMap;
import java.util.Map;

public class TokenizerFabricImpl implements TokenizerFabric {
    private final Map<CommandType, Tokenizer> tokenizerCollection;

    public TokenizerFabricImpl() {
        this.tokenizerCollection = new HashMap<>();
    }

    @Override
    public void registerTokenizer(CommandType type, Tokenizer tokenizer) {
        if (type == null) {
            throw new IllegalArgumentException("Command type object param must be set");
        }
        if (tokenizer == null) {
            throw new IllegalArgumentException("Tokenizer object param must be set");
        }
        tokenizerCollection.put(type, tokenizer);
    }

    @Override
    public Tokenizer getTokenizerForType(CommandType type){
        if (type == null) {
            throw new IllegalArgumentException("Command type object param must be set");
        }
        Tokenizer tokenizer = tokenizerCollection.get(type);
        if (tokenizer == null) {
            String msg = "There is not registered tokenizer for command type \"%s\"";
            throw new IllegalStateException(String.format(msg, type));
        }
        return tokenizer;
    }

}
