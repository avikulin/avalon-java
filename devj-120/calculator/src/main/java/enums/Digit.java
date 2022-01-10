package enums;

import static constants.Constants.*;

public enum Digit {
    DIGIT_A('A', CALCULATION_MODE_HEX),
    DIGIT_B('B', CALCULATION_MODE_HEX),
    DIGIT_C('C', CALCULATION_MODE_HEX),
    DIGIT_D('D', CALCULATION_MODE_HEX),
    DIGIT_E('E', CALCULATION_MODE_HEX),
    DIGIT_F('F', CALCULATION_MODE_HEX),
    DIGIT_0('0', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC | CALCULATION_MODE_BIN),
    DIGIT_1('1', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC | CALCULATION_MODE_BIN),
    DIGIT_2('2', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC),
    DIGIT_3('3', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC),
    DIGIT_4('4', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC),
    DIGIT_5('5', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC),
    DIGIT_6('6', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC),
    DIGIT_7('7', CALCULATION_MODE_HEX | CALCULATION_MODE_OCT | CALCULATION_MODE_DEC),
    DIGIT_8('8', CALCULATION_MODE_HEX | CALCULATION_MODE_DEC),
    DIGIT_9('9', CALCULATION_MODE_HEX | CALCULATION_MODE_DEC);

    private final int calculationModesMask;
    private final char symbol;

    Digit(char symbol, int modeMask) {
        this.symbol = symbol;
        this.calculationModesMask = modeMask;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isModeSupported(CalculationMode mode) {
        int checkedMask = mode.getCalculationModeMask();
        return (calculationModesMask & checkedMask) == checkedMask;
    }

    public String toString(){
        return Character.toString(symbol);
    }
}
