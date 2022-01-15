package ui;

import model.Book;
import model.BooksTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame extends JFrame {
    private final BooksTableModel booksModel;
    private final JTable booksTable;

    public MainFrame()  {
        super("Library books");
        setBounds(300, 200, 600, 400);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveAndExit();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                System.exit(0);
            }
        });

        BooksTableModel btm = null;
        try {
            btm = new BooksTableModel();
        }catch (IOException| ClassNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

        booksModel = btm;
        booksTable = new JTable(booksModel);

        initMenuBar();
        initLayout();
    }

    private void saveAndExit() {
        if (JOptionPane.showConfirmDialog(MainFrame.this,
                "Are you sure?", "Exit confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
            try{
                booksModel.save();
            } catch (IOException e){
                if (JOptionPane.showConfirmDialog(this, "Saving error", "Error",
                                                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE)
                                                    == JOptionPane.NO_OPTION){
                    return;
                }
            }
            dispose();
        }
    }

    private void initLayout() {
        add(booksTable, BorderLayout.CENTER);
        add(booksTable.getTableHeader(), BorderLayout.NORTH);
    }

    private void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Operations");
        menu.setMnemonic(KeyEvent.VK_O);

        JMenuItem menuItem = new JMenuItem("Add...");
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.CTRL_DOWN_MASK|InputEvent.ALT_DOWN_MASK));
        menuItem.addActionListener(e -> addBook());
        menu.add(menuItem);

        menuItem = new JMenuItem("Change...");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                InputEvent.CTRL_DOWN_MASK|InputEvent.ALT_DOWN_MASK));
        menuItem.addActionListener(e -> changeBook());
        menu.add(menuItem);

        menuItem = new JMenuItem("Delete...");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_DOWN_MASK|InputEvent.ALT_DOWN_MASK));
        menuItem.addActionListener(e -> deleteBook());

        menu.add(menuItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void deleteBook() {
        int rowIdx = booksTable.getSelectedRow();
        if (rowIdx == -1){
            return;
        }

        Book book = booksModel.getBook(rowIdx);
        if (
                JOptionPane.showConfirmDialog(this, "Are you shure?",
                                            "Deleting information",
                                                    JOptionPane.YES_NO_OPTION,
                                                    JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
            booksModel.bookDeleting(rowIdx);
        }

    }

    private void addBook(){
        BooksDialog dlg = new BooksDialog(this);
        while (true) {
            dlg.setVisible(true);
            if (!dlg.isOkPressed()){
                return;
            }
            try {
                Book b = new Book(dlg.getBookCodeFieldText(), dlg.getBookISBNFieldText(), dlg.getBookNameFieldText(),
                        dlg.getBookAuthorFieldText(), dlg.getBookPublishYearFieldValue());
                booksModel.addBook(b);
                return;
            }catch (Exception e){
                JOptionPane.showMessageDialog(dlg, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void changeBook(){
        int rowIdx = booksTable.getSelectedRow();
        if (rowIdx == -1){
            return;
        }

        Book b = booksModel.getBook(rowIdx);
        BooksDialog dlg = new BooksDialog(this, b);
        while (true) {
            dlg.setVisible(true);
            if (!dlg.isOkPressed()){
                return;
            }
            try {
                b.setIsbn(dlg.getBookISBNFieldText());
                b.setName(dlg.getBookNameFieldText());
                b.setAuthors(dlg.getBookAuthorFieldText());
                b.setPublishYear(dlg.getBookPublishYearFieldValue());

                booksModel.addBook(b);
                return;
            }catch (Exception e){
                JOptionPane.showMessageDialog(dlg, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
