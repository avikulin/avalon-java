package dal;

import contracts.Extractable;
import contracts.Repository;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.HashSet;
import java.util.Set;

public class RepositoryImpl<T extends Extractable> implements Repository<T> {
    private final ObservableList<T> dataStore;
    private final Set<T> insertedItems;
    private final Set<T> updatedItems;
    private final Set<T> deletedItems; // удаленный объект будет удерживаться этой ссылкой от GC

    public RepositoryImpl() {
        this.insertedItems = new HashSet<>();
        this.updatedItems  = new HashSet<>();
        this.deletedItems  = new HashSet<>();
        this.dataStore = FXCollections.observableArrayList(new Callback<T, Observable[]>() {
            @Override
            public Observable[] call(T param) {
                return param.extractProperties();
            }
        });
        ListChangeListener<T> listener = new ListChangeListener<T>() {
            @Override
            public void onChanged(Change<? extends T> c) {
                while (c.next()) {
                    if (c.wasPermutated()) {
                        // перестановки игнорируем
                    } else if (c.wasUpdated()) {
                        // добавляем ссылки на измененные записи
                        for (int i = c.getFrom(); i < c.getTo(); ++i){
                            updatedItems.add(dataStore.get(i));
                        }
                    } else {
                        // добавляем ссылки на добавленные записи
                        deletedItems.addAll(c.getRemoved());
                        // добавляем ссылки на удаленные записи
                        insertedItems.addAll(c.getAddedSubList());
                    }
                }
            }
        };
        this.dataStore.addListener(listener);
    }

    @Override
    public void bind(TableView<T> view) {
        if (view == null){
            throw new IllegalArgumentException("Table view object reference must be not null");
        }
        view.setItems(dataStore);
    }

    @Override
    public void addItem(T item) {
        if (item == null){
            throw new IllegalArgumentException("Item object reference must be not null");
        }
        this.dataStore.add(item);
    }

    @Override
    public T getItem(int pos) {
        if (pos <0 || pos > this.dataStore.size() - 1){
            throw new IllegalArgumentException("Item position passed is out of the bounds");
        }
        return this.dataStore.get(pos);
    }

    @Override
    public void deleteItem(T item) {
        if (item == null){
            throw new IllegalArgumentException("Item object reference must be not null");
        }
        this.dataStore.remove(item);
    }

    @Override
    public void saveToDb() {
        System.out.println("ADDED");
        System.out.println("-------------------------------------------");
        for (T item: this.insertedItems){
            System.out.println(item);
        }
        System.out.println("\nUPDATED");
        System.out.println("-------------------------------------------");
        for (T item: this.updatedItems){
            System.out.println(item);
        }
        System.out.println("\nDELETED");
        System.out.println("-------------------------------------------");
        for (T item: this.deletedItems){
            System.out.println(item);
        }

        System.out.println("\n\nPURELY ADDED");
        System.out.println("-------------------------------------------");
        this.insertedItems.removeAll(this.deletedItems);
        for (T item: this.insertedItems){
            System.out.println(item);
        }
        System.out.println("\nPURELY UPDATED");
        System.out.println("-------------------------------------------");
        this.updatedItems.removeAll(this.deletedItems);
        for (T item: this.updatedItems){
            System.out.println(item);
        }
    }
}
