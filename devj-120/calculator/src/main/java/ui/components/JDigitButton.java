package ui.components;

import enums.CalculationMode;
import enums.Digit;
import utils.Tracer;

import javax.swing.*;

public class JDigitButton extends JButton {
    private Digit digit;

    public JDigitButton(Digit digit) {
        super(Character.toString(digit.getSymbol()));
        if (digit == null) {
            String msg = "Digit code param must be not null";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Constructor error");
            throw exception;
        }
        this.digit = digit;
    }

    public boolean checkSupported(CalculationMode mode) {
        return digit.isModeSupported(mode);
    }
}
