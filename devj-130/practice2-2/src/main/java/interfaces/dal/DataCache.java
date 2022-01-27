package interfaces.dal;

import interfaces.metadata.ColumnMetadata;

import java.util.List;

public interface DataCache {
    String getTableName();
    void fillCache(int centerPageId);
    Object getData(int rowId, int columnId);
    List<ColumnMetadata> getDataStructure();
    int getNumberOfRows();
    int getNumberOfColumns();
}
