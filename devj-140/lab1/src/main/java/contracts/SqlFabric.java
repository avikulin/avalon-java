package contracts;

import java.sql.SQLException;
import java.util.List;

public interface SqlFabric<T extends Extractable> {
    void getAllRecords(Class<T> clazz, List<T> receiver);
    void insertRecord(T element) throws SQLException;
    void updateRecord(T element);
    void deleteRecord(T element);
    void createTable(Class<T> clazz);
}
