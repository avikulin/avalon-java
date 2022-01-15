package ui.components;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static constants.Constants.DEFAULT_HAS_HEADER_ROW;
import static constants.Constants.SEPARATOR_COMMA_SYMBOL;

public class JCsvFileChooser extends JFileChooser {
    private JSymbolInput delimiterField;
    private JCheckBox hasTitles;
    public JCsvFileChooser(File defaultPath) {
        super(defaultPath);
        setDialogTitle("Choose CSV-file");

        delimiterField = new JSymbolInput(SEPARATOR_COMMA_SYMBOL);
        hasTitles = new JCheckBox("", DEFAULT_HAS_HEADER_ROW);

        JPanel accessoryPane = new JPanel(new FlowLayout());

        accessoryPane.add(new JFormField(delimiterField, "Delimiter :"));
        accessoryPane.add(new JFormField(hasTitles, "Has header row :"));

        JPanel panel1 = (JPanel)this.getComponent(3);
        JPanel panel2 = (JPanel) panel1.getComponent(3);
        panel2.add(accessoryPane);

        setFileFilter(new FileNameExtensionFilter("CSV-files", "csv"));
    }

    public char getDelimiter(){
        return delimiterField.getText().charAt(0);
    }

    public boolean hasTitleRow(){
        return hasTitles.isSelected();
    }
}
