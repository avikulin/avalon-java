package service.lexer.tokenizer;

import contracts.businesslogic.fabrics.TokenFabric;
import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.dataobjects.Token;

import java.util.ArrayList;
import java.util.List;
import static constants.Constants.*;

public class SequenceTokenizer implements Tokenizer {
    private TokenFabric classifier;

    public SequenceTokenizer(TokenFabric classifier) {
        if (classifier == null){
            throw new NullPointerException("Classifier object param reference must be set");
        }
        this.classifier = classifier;
    }

    @Override
    public List<Token> tokenize(String source) {
        if (source==null||source.isEmpty()){
            throw new IllegalArgumentException("Source string must be not-null and non-empty");
        }

        List<Token> res = new ArrayList<>();
        String[] exprParts = source.split(Character.toString(SEPARATOR_COMMA_SYMBOL));
        for(String s:exprParts){
            res.add(classifier.detect(s.trim()));
        }
        return res;
    }
}
