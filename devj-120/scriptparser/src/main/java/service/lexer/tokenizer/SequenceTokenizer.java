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
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source string must be not-null and non-empty");
        }

        List<Token> res = new ArrayList<>();
        int sourceLength = source.length() - 1;
        int quotesMatcher = 0;
        int nextStartPos = 0;

        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch == SEPARATOR_QUOTE_SYMBOL) {
                quotesMatcher++;
            }
            if (ch == SEPARATOR_COMMA_SYMBOL) {
                //не учитываем запятаи внутри литералов
                if (quotesMatcher % 2 == 0) {
                    String literal = source.substring(nextStartPos, i);
                    res.add(classifier.detect(literal.trim()));
                    //запоминаем начальную позицию токена для сл. итерации
                    nextStartPos = Math.min(i + 1, sourceLength);
                }
            }
        }

        if (nextStartPos < sourceLength) {
            String lastToken = source.substring(nextStartPos, sourceLength + 1);
            res.add(classifier.detect(lastToken.trim()));
        }

        /*// не встретилось ни одной запятаи
        if (nextStartPos == RESULT_NOT_FOUND){
            res.add(classifier.detect(source.trim()));
        }
*/
/*        String[] exprParts = source.split(Character.toString(SEPARATOR_COMMA_SYMBOL));
        for (String s : exprParts) {
            res.add(classifier.detect(s.trim()));
        }*/

        return res;
    }
}
