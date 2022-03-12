package contracts;

import javafx.scene.control.TableView;

public interface Repository<T> {
    void bind(TableView<T> view);
    void addItem(T item);
    T getItem(int pos);
    void deleteItem(T item);
    void saveToDb();
}
