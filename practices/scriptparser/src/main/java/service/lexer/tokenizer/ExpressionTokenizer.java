package service.lexer.tokenizer;

import contracts.businesslogic.fabrics.TokenFabric;
import contracts.businesslogic.tokenizers.Tokenizer;
import contracts.dataobjects.Token;
import enums.OperationType;
import enums.TokenType;

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

    private void ExtractSubSequence(String source, int prevPos, int currentPos,List<Token> resultSet){
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
                case SEPARATOR_OPEN_BRACKET:
                case SEPARATOR_CLOSE_BRACKET:{
                    ExtractSubSequence(source, prevTokenRightBound, i, res);
                    prevTokenRightBound = i;
                    break;
                }

                //обрабатываем '-' отдельно, так как он может означать как операцию, так и отрицательное число
                case OPERATION_SYMBOL_SUB:{
                    TokenType prevTokenType = null;
                    if (!res.isEmpty()){
                        Token t = res.get(res.size()-1);
                        prevTokenType = t.getType();
                    }
                    /* если знак минус стоит в самом начале строки, или после открывающей скобки или
                       после другой операции - это означает отр. число
                       такой токен выделять не нужно, так как это скаляр

                       поскольку мы разделяем последовательность знаками операции, значит обрабатываются только те
                       знаки '-', которые стоят после числа, закрывающей скобки, переменной или функции
                    */

                    boolean subSequenceIsEmpty = source.substring(prevTokenRightBound+1, i).trim().isEmpty();
                    if (
                            (
                                prevTokenType == null || prevTokenType == TokenType.RIGHT_BRACKET ||
                                prevTokenType == TokenType.LEFT_BRACKET || prevTokenType == TokenType.OPERATION
                            ) && !subSequenceIsEmpty
                    ){

                        ExtractSubSequence(source, prevTokenRightBound, i, res);
                        prevTokenRightBound = i;
                    }
                    break;

                }
                default:{
                    OperationType opType = OperationType.fromChar(ch);
                    if (opType==OperationType.SUM || opType == OperationType.MUL || opType == OperationType.DIV){
                        ExtractSubSequence(source, prevTokenRightBound, i, res);
                        prevTokenRightBound = i;
                    }
                }
            }
        }

        //проверяем оставшийся суффикс после последнего разделителя
        if (prevTokenRightBound < source.length()-1){
            String token = source.substring(prevTokenRightBound+1);
            res.add(classifier.detect(token.trim()));
        }

        return res;
    }
}
