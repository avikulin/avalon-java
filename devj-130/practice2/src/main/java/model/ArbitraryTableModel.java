package model;

import interfaces.metadata.ColumnMetadata;
import interfaces.dal.DataCache;
import utils.Tracer;

import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.List;

public class ArbitraryTableModel implements javax.swing.table.TableModel {
    private DataCache context;
    private List<ColumnMetadata> meta;
    private List<TableModelListener> modelListeners;

    public ArbitraryTableModel(DataCache context) {
        if (context == null) {
            IllegalArgumentException throwable = new IllegalArgumentException("Database context reference " +
                    "param must be not null");
            Tracer.get().logError(this.getClass(), throwable, "Error in creating the TableModel instance");
            throw throwable;
        }
        this.context = context;
        this.meta = context.getDataStructure();
        this.modelListeners = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return context.getNumberOfRows();
    }

    @Override
    public int getColumnCount() {
        return context.getNumberOfColumns();
    }

    private void checkRowIndex(int rowIndex) {
        int rowsCount = getRowCount();
        if (rowIndex < 0 || rowIndex > rowsCount - 1) {
            IllegalArgumentException throwable = new IllegalArgumentException(
                    String.format("Row index must be between 1 and %d", rowsCount));
            Tracer.get().logError(this.getClass(), throwable,
                    "Error in getting column name for ID = ".concat(Integer.toString(rowIndex)));
            throw throwable;
        }
    }

    private void checkColumnIndex(int columnIndex) {
        int columnCount = getColumnCount();
        if (columnIndex < 0 || columnIndex > columnCount - 1) {
            IllegalArgumentException throwable = new IllegalArgumentException(
                    String.format("Column index must be between 1 and %d", columnCount));
            Tracer.get().logError(this.getClass(), throwable,
                    "Error in getting column name for ID = ".concat(Integer.toString(columnIndex)));
            throw throwable;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        checkColumnIndex(columnIndex);
        return this.meta.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        checkColumnIndex(columnIndex);
        return this.meta.get(columnIndex).getDataType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; //all cells are read only
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        checkRowIndex(rowIndex);
        checkColumnIndex(columnIndex);
        return context.getData(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // all data ara read-only
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.modelListeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.modelListeners.remove(l);
    }
}
