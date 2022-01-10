package fabrics;

import contracts.businesslogic.fabrics.TokenFabric;
import contracts.dataobjects.Token;
import dto.StatementToken;
import enums.FunctionType;
import enums.OperationType;
import enums.TokenType;

import java.util.regex.Pattern;

import static constants.Constants.*;

public class TokenFabricImpl implements TokenFabric {
    private Pattern variableMatcher;
    private Pattern decimalMatcher;

    public TokenFabricImpl() {
        variableMatcher = Pattern.compile("^\\$[a-zA-Z](\\w*)$");
        decimalMatcher = Pattern.compile("-?\\d+(\\.\\d+)?");
    }

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

        //числовой литерал: ####.##,####,-####.##,-####
        if (decimalMatcher.matcher(source).matches()) {
            return createNumberLiteralToken(source);
        }

        //переменная: первым стоит символ "$", после чего как минимум 1 буква, потом буквы, цифры, подчеркивание
        if (variableMatcher.matcher(source).matches()) {
            return createVariableToken(source);
        }

        //операция: 1 символ, совпадает с одним из знаков операции
        if (source.length() == OperationType.getLiteralLength() &&
                OperationType.fromChar(firstChar) != OperationType.NOT_AN_OPERATION) {
            return createOperationToken(source);
        }

        //функция: посимвольно совпадает с именем одной из функций
        if (FunctionType.fromString(source) != FunctionType.NOT_A_FUNCTION) {
            return createFunctionToken(source);
        }

        //строковой литерал: заключен в символы двойных кавычек на концах строки
        if (firstChar == SEPARATOR_QUOTE_SYMBOL && lastChar == SEPARATOR_QUOTE_SYMBOL) {
            return createStringLiteralToken(source.substring(1, sourceLength - 1));
        }

        //если не подошло ни одно из правил, значит токен не совпадет ни с одной из грамматик
        throw new IllegalArgumentException("String is not a valid statement: ".concat(source));
    }
}
