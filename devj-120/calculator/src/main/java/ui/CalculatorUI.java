package ui;

import enums.CalculationMode;
import enums.Digit;
import enums.Operation;
import enums.OperationType;
import model.CalcProcessor;
import ui.components.JDisplayPane;
import ui.components.JDigitButton;
import ui.components.JModeButton;
import utils.Tracer;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import static utils.ButtonBuilder.createButton;

public class CalculatorUI extends JFrame {
    private CalcProcessor calcProcessor;
    private CalculationMode mode;

    private JDisplayPane screen;
    private JPanel panelDigits;
    private JPanel panelModes;

    JTextArea txtCalculationLog;
    JTextArea txtDiagnosticLog;

    public CalculatorUI() {
        super("Engineering calculator © 2022 Avalon Corp.");
        setBounds(1100, 200, 570, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.mode = CalculationMode.DEC;

        initLayout();
        calcProcessor = new CalcProcessor(this.mode);
        calcProcessor.resetState();
        processMode(this.mode);
    }

    private void initLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));

        screen = new JDisplayPane();
        mainPanel.add(screen, BorderLayout.NORTH);

        panelDigits = new JPanel(new GridLayout(4, 4));
        panelDigits.add(createButton(Digit.DIGIT_C, e -> screen.processDigit(Digit.DIGIT_C)));
        panelDigits.add(createButton(Digit.DIGIT_D, e -> screen.processDigit(Digit.DIGIT_D)));
        panelDigits.add(createButton(Digit.DIGIT_E, e -> screen.processDigit(Digit.DIGIT_E)));
        panelDigits.add(createButton(Digit.DIGIT_F, e -> screen.processDigit(Digit.DIGIT_F)));
        panelDigits.add(createButton(Digit.DIGIT_8, e -> screen.processDigit(Digit.DIGIT_8)));
        panelDigits.add(createButton(Digit.DIGIT_9, e -> screen.processDigit(Digit.DIGIT_9)));
        panelDigits.add(createButton(Digit.DIGIT_A, e -> screen.processDigit(Digit.DIGIT_A)));
        panelDigits.add(createButton(Digit.DIGIT_B, e -> screen.processDigit(Digit.DIGIT_B)));
        panelDigits.add(createButton(Digit.DIGIT_4, e -> screen.processDigit(Digit.DIGIT_4)));
        panelDigits.add(createButton(Digit.DIGIT_5, e -> screen.processDigit(Digit.DIGIT_5)));
        panelDigits.add(createButton(Digit.DIGIT_6, e -> screen.processDigit(Digit.DIGIT_6)));
        panelDigits.add(createButton(Digit.DIGIT_7, e -> screen.processDigit(Digit.DIGIT_7)));
        panelDigits.add(createButton(Digit.DIGIT_0, e -> screen.processDigit(Digit.DIGIT_0)));
        panelDigits.add(createButton(Digit.DIGIT_1, e -> screen.processDigit(Digit.DIGIT_1)));
        panelDigits.add(createButton(Digit.DIGIT_2, e -> screen.processDigit(Digit.DIGIT_2)));
        panelDigits.add(createButton(Digit.DIGIT_3, e -> screen.processDigit(Digit.DIGIT_3)));
        mainPanel.add(panelDigits, BorderLayout.WEST);

        JPanel panelOperations = new JPanel(new GridLayout(4, 3));
        panelOperations.add(createButton("shr", e -> processOperation(Operation.SHR)));
        panelOperations.add(createButton("xor", e -> processOperation(Operation.XOR)));
        panelOperations.add(createButton("/", e -> processOperation(Operation.DIV)));

        panelOperations.add(createButton("shl", e -> processOperation(Operation.SHL)));
        panelOperations.add(createButton("and", e -> processOperation(Operation.AND)));
        panelOperations.add(createButton("*", e -> processOperation(Operation.MUL)));

        panelOperations.add(createButton("CE", e -> resetState()));
        panelOperations.add(createButton("or", e -> processOperation(Operation.OR)));
        panelOperations.add(createButton("-", e -> processOperation(Operation.SUB)));

        panelOperations.add(createButton("±", e -> processOperation(Operation.CHANGE_SIGN)));
        panelOperations.add(createButton("not", e -> processOperation(Operation.NOT)));
        panelOperations.add(createButton("+", e -> processOperation(Operation.SUM)));
        mainPanel.add(panelOperations, BorderLayout.CENTER);

        panelModes = new JPanel(new GridLayout(4, 1));

        panelModes.add(createButton(CalculationMode.DEC, e -> processMode(CalculationMode.DEC)));
        panelModes.add(createButton(CalculationMode.BIN, e -> processMode(CalculationMode.BIN)));
        panelModes.add(createButton(CalculationMode.OCT, e -> processMode(CalculationMode.OCT)));
        panelModes.add(createButton(CalculationMode.HEX, e -> processMode(CalculationMode.HEX)));
        mainPanel.add(panelModes, BorderLayout.EAST);

        mainPanel.add(createButton("=", e -> performCalculation()), BorderLayout.SOUTH);

        JPanel calculationLogPane = new JPanel(new BorderLayout());
        JLabel lbCalculationLog = new JLabel("Calculation history:");
        txtCalculationLog = new JTextArea();
        txtCalculationLog.setEditable(false);

        JScrollPane spCalculationLog = new JScrollPane(txtCalculationLog);
        spCalculationLog.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spCalculationLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        calculationLogPane.add(lbCalculationLog,BorderLayout.NORTH);
        calculationLogPane.add(spCalculationLog, BorderLayout.CENTER);

        JPanel diagnosticLogPane = new JPanel(new BorderLayout());
        JLabel lbDiagnosticLog = new JLabel("Diagnostic info:");
        txtDiagnosticLog = new JTextArea();
        txtDiagnosticLog.setEditable(false);
        JScrollPane spDiagnosticLog = new JScrollPane(txtDiagnosticLog);
        spDiagnosticLog.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spDiagnosticLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        diagnosticLogPane.add(lbDiagnosticLog,BorderLayout.NORTH);
        diagnosticLogPane.add(spDiagnosticLog, BorderLayout.CENTER);

        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.add("Calculation", mainPanel);
        mainTabs.add("History", calculationLogPane);
        mainTabs.add("Diagnostic", diagnosticLogPane);
        mainTabs.setSelectedIndex(0);

        mainTabs.addChangeListener(x->{
            if (mainTabs.getSelectedIndex() == 1) {
                txtCalculationLog.setText(calcProcessor.getHistory());
                DefaultCaret caret = (DefaultCaret) txtCalculationLog.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            }
            if (mainTabs.getSelectedIndex() == 2) {
                txtDiagnosticLog.setText(Tracer.get().getLog());
                DefaultCaret caret = (DefaultCaret) txtDiagnosticLog.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            }
        });

        add(mainTabs);
    }

    private void processOperation(Operation operation) {
        try {
            if (screen.isModifiedState()) {
                calcProcessor.enterOperand(screen.getDisplayValue());
            }
            String value = calcProcessor.enterOperation(operation);
            if (operation.getType() == OperationType.UNARY) {
                screen.displayValue(value, true);
            } else {
                screen.reset();
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processMode(CalculationMode mode) {
        this.mode = mode;
        screen.setMode(mode);
        calcProcessor.setMode(mode);

        for (Component c : panelDigits.getComponents()) {
            JDigitButton btn = (JDigitButton) c;
            btn.setEnabled(btn.checkSupported(mode));
        }

        Color standardColor = panelModes.getBackground();
        for (Component c : panelModes.getComponents()) {
            JModeButton btn = (JModeButton) c;
            Color color = (btn.getMode() == mode) ? Color.orange : standardColor;
            btn.setBackground(color);
        }
    }

    private void performCalculation() {
        try {
            calcProcessor.enterOperand(screen.getDisplayValue());
            String res = calcProcessor.processCalculation();
            screen.displayValue(res, true);
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetState() {
        calcProcessor.resetState();
        screen.reset();
    }

    public static void main(String[] args) {
        new CalculatorUI().setVisible(true);
    }
}
