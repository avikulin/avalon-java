package fabrics;

import contracts.businesslogic.fabrics.TokenFabric;
import contracts.dataobjects.Token;
import dto.StatementToken;
import enums.FunctionType;
import enums.OperationType;
import enums.SeparatorType;
import enums.TokenType;

import static constants.Constants.*;

public class TokenFabricImpl implements TokenFabric {
    @Override
    public Token createLBracketToken(String source) {
        return new StatementToken(TokenType.LEFT_BRACKET, source);
    }

    @Override
    public Token createRBracketToken(String source) {
        return new StatementToken(TokenType.RIGHT_BRACKET, source);
    }

    @Override
    public Token createStringLiteralToken(String source) {
        return new StatementToken(TokenType.STRING_LITERAL, source);
    }

    @Override
    public Token createNumberLiteralToken(String source) {
        return new StatementToken(TokenType.NUMBER_LITERAL, source);
    }

    @Override
    public Token createVariableToken(String source) {
        return new StatementToken(TokenType.VARIABLE, source);
    }

    @Override
    public Token createFunctionToken(String source) {
        return new StatementToken(TokenType.FUNCTION, source);
    }

    @Override
    public Token createOperationToken(String source) {
        return new StatementToken(TokenType.OPERATION, source);
    }

    @Override
    public Token detect(String source) {
        char firstChar = source.charAt(0);
        char lastChar = source.charAt(source.length() - 1);
        int sourceLength = source.length();

        //левая скобка: длина 1 символ, и первым стоит символ "("
        if (firstChar == SEPARATOR_OPEN_BRACKET && sourceLength == 1) {
            return createLBracketToken(source);
        }
        //правая скобка: длина 1 символ, и первым стоит символ "("
        if (firstChar == SEPARATOR_CLOSE_BRACKET && sourceLength == 1) {
            return createRBracketToken(source);
        }

        //числовой литерал: начинается с цифры
        if (Character.isDigit(firstChar)) {
            return createNumberLiteralToken(source);
        }

        //переменная: первым стоит символ "$", после чего как минимум 1 символ
        if (SeparatorType.isVariableMarker(firstChar) && source.length() > VARIABLE_LITERAL_LENGTH) {
            return createVariableToken(source);
        }

        //операция: 1 символ, совпадает с одним из знаков операции
        if (source.length() == OperationType.getLiteralLength() &&
                OperationType.fromChar(firstChar) != OperationType.NOT_AN_OPERATION) {
            return createOperationToken(source);
        }

        //функция: 3 символа, совпадают с именем одной из функций
        if (source.length() == FunctionType.getDisplayNameLength() &&
                FunctionType.fromString(source) != FunctionType.NOT_A_FUNCTION) {
            return createFunctionToken(source);
        }

        //строковой литерал: если не подошло ни одно из вышестоящих правил
        if (firstChar == SEPARATOR_QUOTE_SYMBOL && lastChar == SEPARATOR_QUOTE_SYMBOL) {
            return createStringLiteralToken(source.substring(1, sourceLength - 1));
        }

        //если не подошло ни одно из правил, значит токен не совпадет ни с одной из грамматик
        throw new IllegalArgumentException("String is not a valid statement");
    }
}
