package ui.components;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JSymbolInput extends JTextField {
    public JSymbolInput(char value) {
        setColumns(2);
        setText(Character.toString(value));
        setHorizontalAlignment(SwingConstants.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (getText().length() >= 1){
                    e.consume();
                }
            }
        });
    }
}
