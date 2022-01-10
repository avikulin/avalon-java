package service.grammar;

import contracts.businesslogic.analyzers.GrammarAnalyzer;
import contracts.businesslogic.fabrics.ExpressionFabric;
import contracts.dataobjects.Expression;
import contracts.dataobjects.Token;
import dto.OperationSearchResult;
import enums.FunctionType;
import enums.OperationType;
import enums.TokenType;

import java.util.*;

import static constants.Constants.RESULT_NOT_FOUND;

public class ExpressionGrammarParser implements GrammarAnalyzer {
    //кэшируем только ссылки, поэтому кэш живет столько же, сколько выполняется скрипт
    private final ExpressionFabric exprNodeFabric;

    public ExpressionGrammarParser(ExpressionFabric expressionFabric) {
        if (expressionFabric == null) {
            throw new NullPointerException("Expression fabric object param must be not null");
        }
        this.exprNodeFabric = expressionFabric;
    }

    private boolean isBracketsMatch(final List<Token> source, int startPos, int endPos){
        int numberOfLeftBracketsBetween = 0;
        for (int i=startPos; i <=endPos;i++){
            if(source.get(i).getType() == TokenType.LEFT_BRACKET){
                numberOfLeftBracketsBetween++;
            }
            if(source.get(i).getType() == TokenType.RIGHT_BRACKET){
                numberOfLeftBracketsBetween--;
                numberOfLeftBracketsBetween = Math.max(0, numberOfLeftBracketsBetween);
            }
        }
        return numberOfLeftBracketsBetween == 0;
    }

    private OperationSearchResult findLastMinimalPriorityOperation(final List<Token> source, int startPos, int endPos) {
        Deque<Integer> bracketsMatch = new ArrayDeque<>();
        int operationPos = RESULT_NOT_FOUND;
        char operationSymbol = 0;

        startPos = Math.min(startPos, source.size() - 1);
        endPos = Math.min(endPos, source.size() - 1);

        for (int i = startPos; i <= endPos; i++) {
            Token token = source.get(i);
            switch (token.getType()) {
                case LEFT_BRACKET: {
                    bracketsMatch.push(i);
                    break;
                }
                case RIGHT_BRACKET: {
                    if (bracketsMatch.isEmpty()){
                        String msg = "Expression string passed has an inconsistency between " +
                                     "left and right brackets at pos. %d";
                        throw new IllegalArgumentException(String.format(msg, i));
                    }
                    bracketsMatch.pop();
                    break;
                }
                case OPERATION: {
                    if (!bracketsMatch.isEmpty()) {
                        break; // пропускаем операции внутри подвыражений в скобках.
                        // анализируем только операции в основном выражении
                    }

                    OperationType type = OperationType.fromToken(token);
                    //нашли знак * или /, если до этого других знаков не было
                    if (type.getPriority() == 1 && operationPos == -1) {
                        operationPos = i;
                        operationSymbol = type.getSymbol();
                        break;
                    }

                    //ищем знак + или -, если до этого были * ли / - заменяем
                    if (type.getPriority() == 2) {
                        operationPos = i;
                        operationSymbol = type.getSymbol();
                    }
                }
            }
        }
        if (!bracketsMatch.isEmpty()) {
            throw new IllegalArgumentException("Expression string passed have inconsistency " +
                    "in open and closing brackets");
        }

        return new OperationSearchResult(operationPos, operationSymbol);
    }

    private Expression parseExpression(final List<Token> source, int startPos, int endPos, int sequenceId) {
        // общие проверки на валидность параметров
        if (startPos < 0 || endPos > source.size() - 1) {
            throw new IndexOutOfBoundsException("Passed bound parameters are incorrect");
        }

        if (startPos > endPos) {
            throw new IllegalArgumentException("Start position param should less or equal the end position param");
        }

        Token firstToken = source.get(startPos);
        TokenType fistTokenType = firstToken.getType();

        Token endToken = source.get(endPos);
        TokenType endTokenType = endToken.getType();

        /* поскольку мы рекурсивно разбиваем выражение по самому правой наименее приоритетной операции,
         *  возможно 4 рекурсивных варианта:
         * а) пришло обычное выражение(есть операции верхнего уровня):
         *                                                               $x+1,
         *                                                               $y*2,
         *                                                               ($x+1)-$y*2,
         *                                                               ($x+1)+($y*2),
         *                                                               sin($x+1)/tg($y*2)
         *                                                               ^
         *                                                               | рекуррентный случай (углубляем рекурсию)
         *
         * б) пришлая функция(первым токеном идет функция, потом подвыражение в скобказ):<--рекуррентный случай
         *                                                               sin($x+1),
         *                                                               tg($y),
         *                                                               cos(5.2)
         *                                                               ^
         *                                                               | рекуррентный случай (углубляем рекурсию)
         * в) пришло подвыражение (заключено в скобки):
         *                                                               ($x+1),
         *                                                               ($y*2+5-$z/3),
         *                                                               (1+5.3)
         *                                                               ^
         *                                                               | рекуррентный случай (углубляем рекурсию)
         *
         * г) пришел скалярный аргумент (единственный токен с типом NUMBER_LITERAL или VARIABLE):
         *                                                               1,
         *                                                               $x
         *                                                               ^
         *                                                               | базовый случай (выходим из рекурси)

         * */

        // обрабатываем вариант г) - базовый случай рекурсии
        if (startPos == endPos) {
            Token token = firstToken;
            switch (fistTokenType) {
                case VARIABLE:
                    return exprNodeFabric.createVariableNode(token.getSource());
                case NUMBER_LITERAL: {
                    try {
                        double value = Double.parseDouble(token.getSource());
                        return exprNodeFabric.createValueNode(value);
                    } catch (NumberFormatException ex) {
                        String msg = "The expression part\"%s\"(pos. %d) should be a valid decimal value";
                        throw new IllegalArgumentException(String.format(msg, token.getSource(), startPos));
                    }
                }
                default: {
                    String msg = "Expression passed has a bad structure: part \"%s\"(pos. %d) should be a scalar";
                    throw new IllegalArgumentException(String.format(msg, token.getSource(), startPos));
                }
            }
        }

        // обрабатываем вариант в)
        if (fistTokenType == TokenType.LEFT_BRACKET &&
                endTokenType == TokenType.RIGHT_BRACKET&&isBracketsMatch(source, startPos+1, endPos-1)){
            if (endPos - startPos > 1) {
                //раскрываем скобки и передаем на следующий уровень рекурсии
                int subExprStart = startPos + 1;
                int subExprEnd = endPos - 1;
                Expression subExpression = parseExpression(source, subExprStart, subExprEnd, sequenceId);
                return subExpression;
            }

            String msg = "Expression string passed have inconsistency in open and closing brackets at pos. %d";
            throw new IllegalArgumentException(String.format(msg, startPos));
        }

        // обрабатываем вариант б)
        if (fistTokenType == TokenType.FUNCTION && endTokenType == TokenType.RIGHT_BRACKET) {
            /*int numberOfLeftBracketsBetween = 0;
            for (int i=startPos + 2; i <endPos;i++){
                if(source.get(i).getType() == TokenType.LEFT_BRACKET){
                  numberOfLeftBracketsBetween++;
                }
                if(source.get(i).getType() == TokenType.RIGHT_BRACKET){
                    numberOfLeftBracketsBetween--;
                    numberOfLeftBracketsBetween = Math.max(0, numberOfLeftBracketsBetween);
                }
            }*/
            if (endPos - startPos > 2 &&
                source.get(startPos + 1).getType() == TokenType.LEFT_BRACKET &&
                isBracketsMatch(source, startPos +2, endPos - 1))
            {
                FunctionType fun = FunctionType.fromString(firstToken.getSource());
                //раскрываем скобки и передаем на следующий уровень рекурсии
                int subExprStart = startPos + 2;
                int subExprEnd = endPos - 1;
                Expression funSubExpression = parseExpression(source, subExprStart, subExprEnd, sequenceId);
                //формируем функциональный узел
                return exprNodeFabric.createFunctionNode(fun, funSubExpression);
            }
        }

        // обрабатываем вариант а)

        // проверяем, что у нас на вход пришло не менее 3-х токенов: 2 операнда и 1 операция
        if (endPos - startPos > 1) {
            OperationSearchResult osr = findLastMinimalPriorityOperation(source, startPos, endPos);
            int operatorPos = osr.getOperationPos();
            if (osr.getOperationPos() != RESULT_NOT_FOUND && operatorPos != startPos && operatorPos != endPos) {
                OperationType operationType = OperationType.fromChar(osr.getOperationSymbol());
                Expression lvalue = parseExpression(source, startPos, operatorPos - 1, sequenceId);
                Expression rvalue = parseExpression(source, operatorPos + 1, endPos, sequenceId);
                Expression expNode = exprNodeFabric.createExpressionNode(operationType, lvalue, rvalue);
                return expNode;
            }
        }

        String msg = "Expression string passed have invalid operation formatting between pos. %d and pos %d";
        throw new IllegalArgumentException(String.format(msg, startPos, endPos));
    }

    @Override
    public Expression parseTokenSequence(final List<Token> source, int sequenceId) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Input source must be not-null and non-empty token sequence");
        }

        return parseExpression(source, 0, source.size() - 1, sequenceId);
    }

    @Override
    public Expression parseScalarToken(Token token, int sequenceId) {
        throw new UnsupportedOperationException("This grammar doesnt support literal tokens");
    }
}
