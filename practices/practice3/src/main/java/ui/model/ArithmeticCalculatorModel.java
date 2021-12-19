package ui.model;

import java.math.BigDecimal;

public interface ArithmeticCalculatorModel {
    // working with context
    int initContext(int value);
    int getContext();

    // binary operations
    void add(BigDecimal value);
    void subtract(int value);
    void multiply(int value);
    void division(int value);

    void binaryAndOperation(int value);
    void binaryOrOperation(int value);
    void binaryXorOperation(int value);
    void binaryNotOperation(int value);

    // unary operations
    void binaryShiftRight();
    void binaryShiftLeft();
    void invertValue();
}
