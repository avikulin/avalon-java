package utils;

import enums.CalculationMode;
import enums.Digit;
import ui.components.JDigitButton;
import ui.components.JModeButton;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ButtonBuilder {
    public static JButton createButton(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setFont(btn.getFont().deriveFont(24.f));
        btn.addActionListener(al);
        return btn;
    }

    public static JButton createButton(Digit digit, ActionListener al) {
        JButton btn = new JDigitButton(digit);
        btn.setFont(btn.getFont().deriveFont(24.f));
        btn.addActionListener(al);
        return btn;
    }

    public static JButton createButton(CalculationMode mode, ActionListener al) {
        JButton btn = new JModeButton(mode.toString(), mode);
        btn.setFont(btn.getFont().deriveFont(24.f));
        btn.addActionListener(al);
        return btn;
    }
}
