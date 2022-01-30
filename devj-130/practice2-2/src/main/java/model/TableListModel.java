package model;

import interfaces.dal.DbConnection;
import utils.Tracer;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class TableListModel implements ComboBoxModel<String> {
    private List<ListDataListener> listeners;
    private List<String> tableNames;
    private String selectedItem;

    public TableListModel(DbConnection dataContext) {
        if (dataContext == null || !dataContext.isReady()) {
            IllegalArgumentException ex = new IllegalArgumentException(
                    "Database connection reference param must be not null and database connection must be initialized");
            Tracer.get().logError(this.getClass(), ex, "Error in creating instance");
            throw ex;
        }
        this.listeners = new ArrayList<>();
        this.tableNames = dataContext.getTables();
        /*if (tableNames.size()>0){
            this.setSelectedItem(tableNames.get(0));
        }*/
    }

    @Override
    public int getSize() {
        return tableNames.size();
    }

    @Override
    public String getElementAt(int index) {
        return tableNames.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if ((selectedItem != null && !selectedItem.equals( anItem )) ||  // подсмотрено в DefaultListModel
                selectedItem == null && anItem != null) {
            selectedItem = (String)anItem;
            fireContentChangedEvent(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,-1,-1));
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    void fireContentChangedEvent(ListDataEvent eventID){
        for(ListDataListener ldl : listeners){
            ldl.contentsChanged(eventID);
        }
    }
}
