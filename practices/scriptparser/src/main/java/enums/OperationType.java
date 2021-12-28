package enums;

import contracts.dataobjects.Token;

import java.util.function.BinaryOperator;

import static constants.Constants.*;

public enum OperationType {
    SUM(OPERATION_SYMBOL_SUM, 2, (x, y) -> x + y, "Summing operation"),
    SUB(OPERATION_SYMBOL_SUB, 2, (x, y) -> x - y, "Subtracting operation"),
    MUL(OPERATION_SYMBOL_MUL, 1, (x, y) -> x * y, "Multiplication operation"),
    DIV(OPERATION_SYMBOL_DIV, 1, (x, y) -> x / y, "Division operation"),
    NOT_AN_OPERATION((char) 0, 3, null, "Illegal operation");


    private final char symbol;
    private final int priority;
    private final String docString;
    private final BinaryOperator<Double> delegate;

    OperationType(char symbol, int priority, BinaryOperator<Double> delegate, String docString) {
        this.symbol = symbol;
        this.priority = priority;
        this.delegate = delegate;
        this.docString = docString;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getPriority() {
        return priority;
    }

    public static int getLiteralLength() {
        return OPERATION_LITERAL_LENGTH;
    }

    static public boolean isOperation(Token token) {
        return fromToken(token) != OperationType.NOT_AN_OPERATION;
    }

    public BinaryOperator<Double> getDelegate() {
        return delegate;
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

    public static String getDescription() {
        StringBuilder res = new StringBuilder();
        res.append("Interpreter supports the following operations:\n");
        String exampleTemplate = "<arg #1 | expression #1> %c <arg #2 | expression #2>";
        for (OperationType type: OperationType.values()){
            if (type != OperationType.NOT_AN_OPERATION){
                res.append(String.format("%c : %s (priority = %d). Format: %s",
                        type.symbol, type.docString, type.priority, exampleTemplate));
                res.append(System.lineSeparator());
            }
        }
        return res.toString();
    }
}
