package service.lexer.tokenizer;

import contracts.businesslogic.fabrics.TokenFabric;
import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.dataobjects.Token;
import enums.OperationType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static constants.Constants.*;

public class ExpressionTokenizer implements Tokenizer {
    private final TokenFabric classifier;

    public ExpressionTokenizer(TokenFabric classifier) {
        if (classifier == null) {
            throw new NullPointerException("Token fabric object reference must be not-null");
        }
        this.classifier = classifier;
    }

    private void ExtractSubSequence(String source, int prevPos, int currentPos,
                                    Consumer<String> delegate, List<Token> resultSet){
        int leftPos = Math.min(prevPos+1, source.length()-1);
        if (leftPos < currentPos){
            String token = source.substring(leftPos, currentPos).trim();
            if (!token.isEmpty()) {
                resultSet.add(classifier.detect(token));
            }
        }
        resultSet.add(classifier.detect(Character.toString(source.charAt(currentPos))));
    }

    @Override
    public List<Token> tokenize(String source) {
        if (source==null||source.isEmpty()){
            throw new IllegalArgumentException("Source string must be not-null and non-empty");
        }

        List<Token> res = new ArrayList<>();
        int prevTokenRightBound = -1;
        for (int i=0; i<source.length();i++){
            char ch = source.charAt(i);
            switch (ch){
                case SEPARATOR_OPEN_BRACKET:{
                    ExtractSubSequence(source, prevTokenRightBound, i, classifier::createLBracketToken, res);
                    prevTokenRightBound = i;
                    break;
                }

                case SEPARATOR_CLOSE_BRACKET:{
                    ExtractSubSequence(source, prevTokenRightBound, i, classifier::createRBracketToken, res);
                    prevTokenRightBound = i;
                    break;
                }

                default:{
                    if (OperationType.fromChar(ch)!=OperationType.NOT_AN_OPERATION){
                        ExtractSubSequence(source, prevTokenRightBound, i, classifier::createOperationToken, res);
                        prevTokenRightBound = i;
                    }
                }
            }
        }

        //проверяем оставшийся суффикс после последнего разделителя
        if (prevTokenRightBound < source.length()-1){
            String token = source.substring(prevTokenRightBound+1);
            res.add(classifier.detect(token));
        }

        return res;
    }
}
