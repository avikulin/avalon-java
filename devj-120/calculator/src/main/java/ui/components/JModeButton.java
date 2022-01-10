package ui.components;

import enums.CalculationMode;
import utils.Tracer;

import javax.swing.*;

public class JModeButton extends JButton {
    private CalculationMode mode;

    public JModeButton(String text, CalculationMode mode) {
        super(text);
        if (mode == null || text == null || text.isEmpty()){
            String msg = "Text and calculation mode params are mandatory";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Constructor error");
            throw exception;
        }
        this.mode = mode;
    }

    public CalculationMode getMode() {
        return mode;
    }
}
