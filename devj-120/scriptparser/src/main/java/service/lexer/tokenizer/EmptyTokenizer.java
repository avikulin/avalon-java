package service.lexer.tokenizer;

import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.dataobjects.Token;

import java.util.ArrayList;
import java.util.List;

public class EmptyTokenizer implements Tokenizer {
    @Override
    public List<Token> tokenize(String source) {
        return new ArrayList<>();
    }
}
