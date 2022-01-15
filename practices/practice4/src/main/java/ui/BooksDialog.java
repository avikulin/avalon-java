package ui;

import model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BooksDialog extends JDialog {
    private final JTextField bookCodeField = new JTextField(10);
    private final JTextField bookISBNField = new JTextField(10);
    private final JTextField bookNameField = new JTextField(50);
    private final JTextField bookAuthorField = new JTextField(50);
    private final JTextField bookPublishYearField = new JTextField(5);

    private boolean okPressed;


    public boolean isOkPressed() {
        return okPressed;
    }

    private void initDialog(){
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowGainedFocus(e);
                okPressed = false;
            }

            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                setLocationRelativeTo(getOwner());
                bookCodeField.requestFocus();
            }
        });
    }

    public BooksDialog(JFrame owner) {
        super(owner, "New book registration",true);
        initDialog();
        initLayout();
    }

    public BooksDialog (JFrame owner, Book book){
        super(owner, "Book editing", true);

        initDialog();
        initLayout();
        pack();

        bookCodeField.setText(book.getBookCode());
        bookISBNField.setText(book.getIsbn());
        bookNameField.setText(book.getName());
        bookAuthorField.setText(book.getAuthors());
        bookPublishYearField.setText(Integer.toString(book.getPublishYear()));
        bookCodeField.setEnabled(false);
    }

    private void initLayout() {
        layoutControls();
        initButtons();
    }

    private void initButtons() {
        JPanel p = new JPanel();

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(e -> {okPressed = true; setVisible(false);});

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {setVisible(false);});

        p.add(okBtn);
        p.add(cancelBtn);

        add(p);
    }

    private void addControlRow(JTextField field, String label, JPanel parent){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel(label);
        lbl.setLabelFor(field);
        p.add(lbl);
        p.add(field);

        parent.add(p, BorderLayout.SOUTH);
    }

    private void layoutControls() {
        JPanel p = new JPanel(null);

        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        addControlRow(bookCodeField, "Code", p);
        addControlRow(bookISBNField, "ISBN", p);
        addControlRow(bookNameField, "Name", p);
        addControlRow(bookAuthorField, "Author(s)", p);
        addControlRow(bookPublishYearField, "Publish year", p);

        add(p, BorderLayout.CENTER);
    }

    public String getBookCodeFieldText(){
        return bookCodeField.getText();
    }

    public String getBookISBNFieldText(){
        return bookISBNField.getText();
    }

    public String getBookNameFieldText(){
        return bookNameField.getText();
    }

    public String getBookAuthorFieldText(){
        return bookAuthorField.getText();
    }

    public int getBookPublishYearFieldValue(){
        return Integer.parseInt(bookPublishYearField.getText());
    }

}
