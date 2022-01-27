package interfaces.dal;

import interfaces.metadata.ColumnMetadata;
import interfaces.metadata.FieldMetadataFabric;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface DbConnection extends Closeable {
    void connectTo(String s) throws IllegalStateException;
    void disconnect() throws SQLException;
    boolean isReady();
    List<String> getTables();
    void setCurrentTable(String tableName);
    String getCurrentTable();
    List<ColumnMetadata> getCurrentTableColumnTypes(FieldMetadataFabric metaFabric);
    Object[][] getData(int pageId);
    int getRowCount();
    int getColumnCount();
    int getNumberOfPages();
}
