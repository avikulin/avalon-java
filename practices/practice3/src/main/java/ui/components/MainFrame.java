package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class MainFrame extends JFrame {
    private JLabel screen;
    private BigDecimal register;
    private boolean nextInputClearsScreen;

    private String prevOp;

    public MainFrame(){
        super("Calculator demo");
        setBounds(300, 200, 600,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        screen = new JLabel("0");
        add(screen, BorderLayout.NORTH);

        screen.setFont(screen.getFont().deriveFont(40.0f));
        screen.setHorizontalAlignment(SwingConstants.RIGHT);
        screen.setBorder(BorderFactory.createEtchedBorder());

        JPanel panel = new JPanel(new GridLayout(4,4));
        panel.add(createButton("7", e->processDigit(7)));
        panel.add(createButton("8", e->processDigit(8)));
        panel.add(createButton("9", e->processDigit(9)));
        panel.add(createButton("+", e->processOp("+")));
        panel.add(createButton("4", e->processDigit(4)));
        panel.add(createButton("5", e->processDigit(5)));
        panel.add(createButton("6", e->processDigit(6)));
        panel.add(createButton("-", e->processOp("-")));
        panel.add(createButton("1", e->processDigit(1)));
        panel.add(createButton("2", e->processDigit(2)));
        panel.add(createButton("3", e->processDigit(3)));
        panel.add(createButton("*", e->processOp("*")));
        panel.add(createButton("0", e->processDigit(0)));
        panel.add(createButton(".", e->processDot()));
        panel.add(createButton("C", e->{screen.setText("0");}));
        panel.add(createButton("/", e->processOp("/")));

        add(panel, BorderLayout.CENTER);
        add(createButton("=", e-> {processOp(null);}), BorderLayout.SOUTH);
    }

    private JButton createButton(String text, ActionListener al){
        JButton btn = new JButton(text);
        btn.setFont(btn.getFont().deriveFont(48.0f));
        btn.addActionListener(al);
        return btn;
    }

    private void processDigit(int digit){
        if (nextInputClearsScreen){
            screen.setText(Integer.toString(digit));
            nextInputClearsScreen = false;
            return;
        }

        if (screen.getText().equals("0")){
            screen.setText(Integer.toString(digit));
        } else {
            screen.setText(screen.getText() + Integer.toString(digit));
        }
    }

    private void processDot(){
        if (nextInputClearsScreen){
            screen.setText("0.");
            nextInputClearsScreen = false;
            return;
        }

        if (!screen.getText().contains(".")) {
            screen.setText(screen.getText() + ".");
        }
    }

    private void processOp(String op){
        BigDecimal screenNum = new BigDecimal(screen.getText());
        if (prevOp!=null){
            switch (prevOp){
                case "+":  register = register.add(screenNum); break;
                case "-": register = register.subtract(screenNum); break;
                case "*": register = register.multiply(screenNum); break;
                case "/": register = register.divide(screenNum); break;
            }
            BigDecimal integerPart = new BigDecimal(register.toBigInteger());
            register = (integerPart.subtract(register).compareTo(register) > 0)?register:integerPart;
            screen.setText(register.toString());
        } else {
            register = screenNum;
        }
        prevOp = op;
        nextInputClearsScreen = true;
    }

}
