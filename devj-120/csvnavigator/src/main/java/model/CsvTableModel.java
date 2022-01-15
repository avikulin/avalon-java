package model;

import contracts.CachedDataSource;
import contracts.DiagnosticLogger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.Tracer;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class CsvTableModel implements TableModel {
    private final CachedDataSource dataSource;
    private final List<TableModelListener> modelListeners;
    private final DiagnosticLogger logger;

    public CsvTableModel(CachedDataSource dataSource) {
        if (dataSource == null){
            throw new IllegalArgumentException("Datasource should be non-null reference object");
        }
        this.dataSource = dataSource;
        this.modelListeners = new ArrayList<>();
        this.logger = Tracer.get();

        logger.logInfo(this.getClass(), "Instance has been created");
    }

    @Override
    public int getRowCount() {
        return this.dataSource.getNumberOfRows();
    }

    @Override
    public int getColumnCount() {
        return this.dataSource.getNumberOfColumns();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.dataSource.getColumnName(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.dataSource.getData(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new NotImplementedException();
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.modelListeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.modelListeners.remove(l);
    }

    public void fireEvent(TableModelEvent tme){
        for (TableModelListener tl :modelListeners){
            tl.tableChanged(tme);
        }
    }

    public void invalidateCache(){
        fireEvent(new TableModelEvent(this, TableModelEvent.HEADER_ROW));   // notifies UI that all the data
                                                                                  // have been changed
        logger.logInfo(this.getClass(), "Cache has been invalidated");
    }
}
