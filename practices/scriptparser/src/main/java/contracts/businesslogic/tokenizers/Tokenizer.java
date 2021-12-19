package contracts.businesslogic.tokenizers;

import contracts.dataobjects.Token;

import java.util.List;

public interface Tokenizer {
    List<Token> tokenize(String source);
}
