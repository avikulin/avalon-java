package enums;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

public enum Operation {
    SUM(OperationType.BINARY, BigInteger::add, "%s + %s"),
    SUB(OperationType.BINARY, BigInteger::subtract, "%s - %s"),
    MUL(OperationType.BINARY, BigInteger::multiply, "%s * %s"),
    DIV(OperationType.BINARY, BigInteger::divide, "%s / %s"),
    AND(OperationType.BINARY, BigInteger::and, "%s AND %s"),
    OR(OperationType.BINARY, BigInteger::or, "%s OR %s"),
    XOR(OperationType.BINARY, BigInteger::xor, "%s XOR %s"),
    NOT(OperationType.UNARY, (x, y) -> x.not(), "NOT(%s)"),
    SHR(OperationType.UNARY, (x, y) -> x.shiftRight(1), "%s >> 1"),
    SHL(OperationType.UNARY, (x, y) -> x.shiftLeft(1), "%s << 1"),
    CHANGE_SIGN(OperationType.UNARY, (x, y) -> x.negate(), "±(%s)");

    private final BinaryOperator<BigInteger> function;
    private final OperationType type;
    private final String template;

    Operation(OperationType type, BinaryOperator<BigInteger> function, String template) {
        this.function = function;
        this.type = type;
        this.template = template;
    }

    public BinaryOperator<BigInteger> getFunction() {
        return function;
    }

    public OperationType getType() {
        return type;
    }

    public String toString(String... operands){
        return String.format(this.template, operands);
    }
}
