package ui;

import contracts.DiagnosticLogger;
import enums.DlgResult;
import ui.components.JFormField;
import ui.components.JSymbolInput;
import utils.Tracer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;

import static constants.Constants.STR_TEMPLATE_TOKEN;

public class CsvOptionsDialog extends JDialog {
    private final JFormField fieldDelimiterSymbol;
    private final JFormField fieldHasTitleRow;
    private final JFormField fieldMaxColumnsToFit;
    private final JFormField fieldColumnNameTemplate;

    private char valueDelimiterSymbol;
    private boolean valueHasTitleRow;
    private int valueMaxColumnsToFit;
    private String valueColumnNameTemplate;
    private final DiagnosticLogger logger;
    private DlgResult result;


    public CsvOptionsDialog(JFrame owner, String title, char delimiterSymbol, boolean hasTitleRow,
                            int maxColumnsWithoutScroll, String columnNameTemplate) {
        super(owner, true);
        this.logger = Tracer.get();

        JPanel root = (JPanel)this.getContentPane();
        root.setBorder(new EmptyBorder(10,10,10,10));

        this.result = DlgResult.EMPTY;
        if (title == null || title.isEmpty()) {
            String msg = "Dialog's title is mandatory param";
            IllegalArgumentException e = new IllegalArgumentException(msg);
            logger.logError(this.getClass(), e, "Invalid dialog param value", msg);
            throw e;
        }
        this.setTitle(title);

        if (columnNameTemplate == null || columnNameTemplate.isEmpty()) {
            String msg = "Column name template is mandatory param";
            IllegalArgumentException e = new IllegalArgumentException(msg);
            logger.logError(this.getClass(), e, "Invalid dialog param value", msg);
            throw e;
        }

        this.fieldDelimiterSymbol = new JFormField(
                new JSymbolInput(delimiterSymbol),
                "Delimiter symbol: "
        );

        this.fieldHasTitleRow = new JFormField(
                new JCheckBox("", hasTitleRow),
                "File has column titles: "
        );

        JFormattedTextField maxColumns = new JFormattedTextField(new NumberFormatter());
        maxColumns.setColumns(4);
        maxColumns.setText(Integer.toString(maxColumnsWithoutScroll));
        this.fieldMaxColumnsToFit = new JFormField(maxColumns,"Max. columns to fit: ");

        this.fieldColumnNameTemplate = new JFormField(
                new JTextField(columnNameTemplate, 15),
                "Template for unnamed columns: "
        );

        JPanel mainBoard = new JPanel(null);
        mainBoard.setLayout(new BoxLayout(mainBoard, BoxLayout.Y_AXIS));
        mainBoard.setBorder(new EmptyBorder(10, 0, 10, 10));

        mainBoard.add(this.fieldDelimiterSymbol);
        mainBoard.add(this.fieldHasTitleRow);
        mainBoard.add(this.fieldMaxColumnsToFit);
        mainBoard.add(this.fieldColumnNameTemplate);

        this.setLayout(new BorderLayout());
        this.add(new JLabel("Settings for processing a CSV-file:"), BorderLayout.NORTH);

        this.add(mainBoard, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOK = new JButton("Save & Close");
        btnOK.addActionListener(e -> {
            try {
                logger.logInfo(this.getClass(), "Applying changed settings");
                validateDialogValues();
                this.result = DlgResult.APPROVED;
                this.setVisible(false);
            } catch (IllegalStateException illegalStateException) {
                String msg =  "Incorrect values detected: ".concat(illegalStateException.getMessage());
                logger.logError(this.getClass(), illegalStateException, "User data validation error", msg);

                JOptionPane.showMessageDialog(
                        this, msg,
                        "Settings error",
                        JOptionPane.ERROR_MESSAGE
                );
                this.setVisible(true);
                this.synchronizeFormFieldsValues();
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> {
            logger.logInfo(this.getClass(), "Discarding changes");
            synchronizeFormFieldsValues();
            this.result = DlgResult.CANCELED;
            this.setVisible(false);
        });

        buttons.add(btnOK);
        buttons.add(btnCancel);

        this.add(buttons, BorderLayout.SOUTH);
        this.pack();

        Tracer.get().logInfo(
                this.getClass(),
                String.format("Instance has been created (initial params: %s)",
                              this.toString())
        );
    }

    private void validateDialogValues() throws IllegalStateException {
        // validating delimiter
        {
            JTextField field = (JTextField) this.fieldDelimiterSymbol.getComponent();
            String value = field.getText();
            if (value.isEmpty()) {
                String msg = "Delimiter symbol must be defined to process the CSV-format";
                throw new IllegalStateException(msg);
            }
            this.valueDelimiterSymbol = value.charAt(0);
        }

        // validating check-box
        {
            JCheckBox field = (JCheckBox) this.fieldHasTitleRow.getComponent();
            this.valueHasTitleRow = field.isSelected();
        }

        // validating maxColumnsToFit
        {
            JTextField field = (JTextField) this.fieldMaxColumnsToFit.getComponent();
            String value = field.getText();

            if (value.isEmpty()) {
                throw new IllegalStateException("Max. columns to fit in window must be defined " +
                        "to properly display the grid");
            }

            try {
                int v = Integer.parseInt(value);
                if (v < 0) {
                    throw new NumberFormatException();
                }
                this.valueMaxColumnsToFit = v;
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Max. columns to fit in window must be a properly " +
                        "formatted integer value over zero");
            }
        }
        //validating column names template
        {
            JTextField field = (JTextField) this.fieldColumnNameTemplate.getComponent();
            String value = field.getText();
            if (value.isEmpty() || value.length() < STR_TEMPLATE_TOKEN.length() || !value.contains(STR_TEMPLATE_TOKEN)) {
                throw new IllegalStateException(String.format("Column name template must be non-empty string with " +
                        "pattern \"%s\" in place of column sequential number", STR_TEMPLATE_TOKEN));
            }
            this.valueColumnNameTemplate = value;
        }
    }

    private void synchronizeFormFieldsValues() {
        logger.logInfo(
                this.getClass(),
                "Synchronizing fields values with internal state: "
                        .concat(this.toString())
        );
        JTextField field1 = (JTextField) this.fieldDelimiterSymbol.getComponent();
        field1.setText(Character.toString(this.valueDelimiterSymbol));
        JCheckBox field2 = (JCheckBox) this.fieldHasTitleRow.getComponent();
        field2.setSelected(this.valueHasTitleRow);
        JTextField field3 = (JTextField) this.fieldMaxColumnsToFit.getComponent();
        field3.setText(Integer.toString(this.valueMaxColumnsToFit));
        JTextField field4 = (JTextField) this.fieldColumnNameTemplate.getComponent();
        field4.setText(this.valueColumnNameTemplate);
    }

    public char getDelimiter() {
        return this.valueDelimiterSymbol;
    }

    public boolean hasTitleRow() {
        return this.valueHasTitleRow;
    }

    public int getMaxColumnsToFit() {
        return this.valueMaxColumnsToFit;
    }

    public String getColumnNameTemplate() {
        return this.valueColumnNameTemplate;
    }

    public DlgResult showModal(){
        logger.logInfo(this.getClass(), "Dialog has been opened");
        setLocationRelativeTo(getOwner());
        setVisible(true); //holds the thread while "visible" becomes false
        return this.result;
    }

    @Override
    public String toString() {
        return "CsvOptionsDialog{" +
                "valueDelimiterSymbol='" + valueDelimiterSymbol +"'"+
                ", valueHasTitleRow=" + valueHasTitleRow +
                ", valueMaxColumnsToFit=" + valueMaxColumnsToFit +
                ", valueColumnNameTemplate='" + valueColumnNameTemplate + '\'' +
                ", result=" + result +
                '}';
    }
}
