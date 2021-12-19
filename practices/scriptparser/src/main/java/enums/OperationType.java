package enums;

import contracts.dataobjects.Token;

import static constants.Constants.*;

public enum OperationType {
    SUM(OPERATION_SYMBOL_SUM,2),
    SUB(OPERATION_SYMBOL_SUB,2),
    MUL(OPERATION_SYMBOL_MUL,1),
    DIV(OPERATION_SYMBOL_DIV,1),
    NOT_AN_OPERATION((char) 0,3);


    private char symbol;
    private int priority;

    OperationType(char symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getPriority() {
        return priority;
    }

    public static int getLiteralLength(){
        return OPERATION_LITERAL_LENGTH;
    }
    static public boolean isOperation(Token token) {
        return fromToken(token)!=OperationType.NOT_AN_OPERATION;
    }

    static public OperationType fromChar(char ch) {
        switch (ch) {
            case OPERATION_SYMBOL_SUM:
                return OperationType.SUM;
            case OPERATION_SYMBOL_SUB:
                return OperationType.SUB;
            case OPERATION_SYMBOL_MUL:
                return OperationType.MUL;
            case OPERATION_SYMBOL_DIV:
                return OperationType.DIV;
        }
        return OperationType.NOT_AN_OPERATION;
    }

    static public OperationType fromToken(Token token) {
        if (token == null) {
            throw new NullPointerException("Token object parameter must be not null");
        }
        if (token.getType() != TokenType.OPERATION) {
            throw new IllegalArgumentException("The token passed is not an operation token");
        }

        String source = token.getSource();
        if (source.length() != 1) {
            throw new IllegalArgumentException("Wrong format of the operation token: ".concat(source));
        }
        return fromChar(source.charAt(0));
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}
