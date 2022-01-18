package enums;

public enum CalculationMode {

    HEX(16, 0b01000),
    DEC(10, 0b00100),
    OCT(8, 0b00010),
    BIN(2, 0b00001);

    private final int radix;
    private final int calculationModeMask;

    CalculationMode(int radix, int calculationMode) {
        this.radix = radix;
        this.calculationModeMask = calculationMode;
    }

    public int getRadix() {
        return radix;
    }

    public int getMask() {
        return calculationModeMask;
    }
}
