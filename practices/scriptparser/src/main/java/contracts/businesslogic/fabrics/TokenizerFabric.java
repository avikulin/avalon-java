package contracts.businesslogic.fabrics;

import contracts.businesslogic.tokenizers.Tokenizer;
import enums.CommandType;

public interface TokenizerFabric {
    void registerTokenizer(CommandType type, Tokenizer tokenizer);
    Tokenizer getTokenizerForType(CommandType type);
}
