package ui.components;

import enums.CalculationMode;
import enums.Digit;
import utils.Tracer;

import javax.swing.*;

public class JDigitButton extends JButton {
    private final Digit digit;

    public JDigitButton(Digit digit) {
        if (digit == null) {
            String msg = "Digit code param must be not null";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Constructor error");
            throw exception;
        }
        this.setText(Character.toString(digit.getSymbol()));
        this.digit = digit;
    }

    public boolean checkSupported(CalculationMode mode) {
        return digit.isModeSupported(mode);
    }
}
