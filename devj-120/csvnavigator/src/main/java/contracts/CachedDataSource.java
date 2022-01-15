package contracts;

import java.io.File;

public interface CachedDataSource {
    void readDataFromFile(File file) throws Exception;

    void reloadDataFromFile() throws Exception;

    void setHeaderRowPresent(boolean headerRowPresent);

    boolean isHeaderRowPresent();

    boolean isReady();

    String getTableName();

    void setColumnNameTemplate(String template);

    String getColumnNamesTemplate();

    Object getData(int rowId, int columnId);

    String getColumnName(int columnId);

    int getNumberOfRows();

    int getNumberOfColumns();

    void reset();
}
