package enums;

public enum Digit {
    DIGIT_A('A', CalculationMode.HEX.getMask()),
    DIGIT_B('B', CalculationMode.HEX.getMask()),
    DIGIT_C('C', CalculationMode.HEX.getMask()),
    DIGIT_D('D', CalculationMode.HEX.getMask()),
    DIGIT_E('E', CalculationMode.HEX.getMask()),
    DIGIT_F('F', CalculationMode.HEX.getMask()),
    DIGIT_0('0', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask() | CalculationMode.BIN.getMask()),
    DIGIT_1('1', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask() | CalculationMode.BIN.getMask()),
    DIGIT_2('2', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask()),
    DIGIT_3('3', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask()),
    DIGIT_4('4', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask()),
    DIGIT_5('5', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask()),
    DIGIT_6('6', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask()),
    DIGIT_7('7', CalculationMode.HEX.getMask() | CalculationMode.OCT.getMask() |
            CalculationMode.DEC.getMask()),
    DIGIT_8('8', CalculationMode.HEX.getMask() | CalculationMode.DEC.getMask()),
    DIGIT_9('9', CalculationMode.HEX.getMask() | CalculationMode.DEC.getMask());

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
        int checkedMask = mode.getMask();
        return (calculationModesMask & checkedMask) == checkedMask;
    }

    public String toString(){
        return Character.toString(symbol);
    }
}
