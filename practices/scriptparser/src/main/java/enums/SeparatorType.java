package enums;

import static constants.Constants.*;

public enum SeparatorType {
    OPEN_BRACKET(SEPARATOR_OPEN_BRACKET),
    CLOSE_BRACKET(SEPARATOR_CLOSE_BRACKET),
    VARIABLE(SEPARATOR_VARIABLE_SYMBOL),
    QUOTE(SEPARATOR_QUOTE_SYMBOL),
    EQUALITY(SEPARATOR_EQUALITY_SYMBOL),
    COMMA(SEPARATOR_COMMA_SYMBOL);

    private char symbol;

    SeparatorType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static boolean isEqualitySymbol(char c) {
        return c == SEPARATOR_EQUALITY_SYMBOL;
    }

    public static boolean isVariableMarker(char c) {
        return c == SEPARATOR_VARIABLE_SYMBOL;
    }

    public static boolean isQuote(char c) {
        return c == SEPARATOR_QUOTE_SYMBOL;
    }

    public static boolean isBracket(char c) {
        return c == SEPARATOR_OPEN_BRACKET || c == SEPARATOR_CLOSE_BRACKET;
    }

    public static boolean isCommaSymbol(char c) {
        return c == SEPARATOR_COMMA_SYMBOL;
    }
}
