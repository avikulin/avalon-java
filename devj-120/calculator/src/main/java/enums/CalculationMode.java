package enums;

import static constants.Constants.*;

public enum CalculationMode {
    HEX(16, CALCULATION_MODE_HEX),
    DEC(10, CALCULATION_MODE_DEC),
    OCT(8, CALCULATION_MODE_OCT),
    BIN(2, CALCULATION_MODE_BIN);

    private final int radix;
    private final int calculationModeMask;

    CalculationMode(int radix, int calculationMode) {
        this.radix = radix;
        this.calculationModeMask = calculationMode;
    }

    public int getRadix() {
        return radix;
    }

    public int getCalculationModeMask() {
        return calculationModeMask;
    }
}
