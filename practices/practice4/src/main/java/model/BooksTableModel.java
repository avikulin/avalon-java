package model;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import repo.BooksList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BooksTableModel implements TableModel {
    private static final String[] COLUMN_HEADERS = {"Code", "ISBN", "Name", "Author(s)", "Publish year" };
    private static final Class<?>[] COLUMN_TYPES = {String.class, String.class,String.class, Integer.class};

    private final BooksList booksList;
    private final List<TableModelListener> modelListeners = new ArrayList<>();

    public BooksTableModel() throws IOException, ClassNotFoundException {
        this.booksList = new BooksList();
    }

    @Override
    public int getRowCount() {
        return booksList.getCount();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_HEADERS.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_HEADERS[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_TYPES[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = booksList.get(rowIndex);
        switch (columnIndex){
            case 0: return book.getBookCode();
            case 1: return book.getIsbn();
            case 2: return book.getName();
            case 3: return book.getAuthors();
            case 4: return book.getPublishYear();
            default: throw new IllegalArgumentException("Unknown column index");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // Should be empty
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        modelListeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        modelListeners.remove(l);
    }

    private void fireTableModelEvent(TableModelEvent tme){
        for (TableModelListener l: modelListeners){
            l.tableChanged(tme);
        }
    }

    public void addBook(Book b){
        booksList.add(b);
        int newRowIndex = booksList.getCount();
        TableModelEvent tme = new TableModelEvent(
                                            this,
                                                   newRowIndex,
                                                   newRowIndex,
                                                   TableModelEvent.ALL_COLUMNS,
                                                   TableModelEvent.INSERT);
        fireTableModelEvent(tme);
    }

    public void bookChanged(int rowIdx){
        TableModelEvent tme = new TableModelEvent(this,
                                                        rowIdx,
                                                        rowIdx,
                                                        TableModelEvent.ALL_COLUMNS,
                                                        TableModelEvent.UPDATE);
        fireTableModelEvent(tme);
    }

    public void bookDeleting(int bookIdx){
        TableModelEvent tme = new TableModelEvent(this,
                                                        bookIdx,
                                                        bookIdx,
                                                        TableModelEvent.ALL_COLUMNS,
                                                        TableModelEvent.DELETE);
        fireTableModelEvent(tme);
    }

    public Book getBook(int idx){
        return booksList.get(idx);
    }

    public void save() throws IOException{
        booksList.save();
    }
}
