package fabrics;

import contracts.businesslogic.fabrics.TokenizerFabric;
import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.businesslogic.utils.DiagnosticLogger;
import enums.CommandType;
import utils.Tracer;

import java.util.HashMap;
import java.util.Map;

public class TokenizerFabricImpl implements TokenizerFabric {
    private final Map<CommandType, Tokenizer> tokenizerCollection;
    private final DiagnosticLogger logger;

    public TokenizerFabricImpl() {
        this.tokenizerCollection = new HashMap<>();
        logger = Tracer.get();
    }

    @Override
    public void registerTokenizer(CommandType type, Tokenizer tokenizer) {
        if (type == null) {
            String msg = "Command type object param must be set";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        if (tokenizer == null) {
            throw new IllegalArgumentException("Tokenizer object param must be set");
        }
        tokenizerCollection.put(type, tokenizer);
    }

    @Override
    public Tokenizer getTokenizerForType(CommandType type){
        if (type == null) {
            String msg = "Command type object param must be set";
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }
        Tokenizer tokenizer = tokenizerCollection.get(type);
        if (tokenizer == null) {
            String msg = String.format("There is not registered tokenizer for command type \"%s\"", type);
            logger.logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }
        return tokenizer;
    }

}
