package dal;

import interfaces.metadata.ColumnMetadata;
import interfaces.dal.DbConnection;
import interfaces.metadata.FieldMetadataFabric;
import utils.Tracer;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static constants.Constants.PAGE_SIZE;

public class DbContext implements DbConnection {
    private Connection connection;
    private static DbContext obj;

    private String tableName;
    private PreparedStatement preparedStatement;
    private int numberOfPages;
    private int numberOfColumns;
    private int numberOfRows;

    private DbContext() {
        init();
        Tracer.get().logInfo(this.getClass(), "Instance created");
    }

    private void init() {
        connection = null;
        preparedStatement = null;
        tableName = null;
        numberOfRows = -1;
        numberOfPages = -1;
        numberOfColumns = -1;
    }

    public static DbConnection get() {
        if (obj == null) {
            obj = new DbContext();
        }
        return obj;
    }

    @Override
    public void connectTo(String s) throws IllegalStateException {
        try {
            connection = DriverManager.getConnection(s);

        } catch (SQLException exception) {
            Tracer.get().logError(this.getClass(), exception,
                    "There are troubles in connection to database: ".concat(s));
            throw new IllegalStateException(exception.getMessage());
        }
        Tracer.get().logInfo(this.getClass(), "Connected: ".concat(s));
    }

    @Override
    public void disconnect() throws SQLException {
        Connection connection = getConnection();
        connection.close();
        init();
        Tracer.get().logInfo(this.getClass(), "Disconnected");
    }

    private Connection getConnection() {
        if (connection == null) {
            IllegalStateException exception = new IllegalStateException("There is now active connection");
            Tracer.get().logError(this.getClass(), exception, "Error in getting connection instance");
            throw exception;
        }
        return connection;
    }

    private PreparedStatement getPreparedStatement() {
        if (preparedStatement == null) {
            IllegalStateException throwable = new IllegalStateException("There is no current table have been set");
            Tracer.get().logError(this.getClass(), throwable, "Error in getting prepared statement");
            throw throwable;
        }
        return preparedStatement;
    }

    @Override
    public boolean isReady() {
        return connection != null;// && preparedStatement != null;
    }

    @Override
    public List<String> getTables() {
        List<String> res = new ArrayList<>();
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet data = stmt.executeQuery("select TABLENAME from SYS.SYSTABLES where TABLETYPE = 'T'");
            while (data.next()) {
                res.add(data.getString(1));
            }
        } catch (SQLException throwable) {
            Tracer.get().logError(this.getClass(), throwable, "Error in getting list of tables");
        }
        Tracer.get().logInfo(this.getClass(),
                String.format("Discover set of tables (%d items): ", res.size())
                        .concat(String.join(", ", res))
        );

        return res;
    }

    @Override
    public void setCurrentTable(String tableName) {
        String sqlQuery = String.format("select * from %s offset ? rows fetch first %d rows only",
                                        tableName, PAGE_SIZE);
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet data = stmt.executeQuery(String.format("select count(*) from %s", tableName));
            data.next();
            preparedStatement = getConnection().prepareStatement(sqlQuery);
            this.tableName = tableName;
            this.numberOfColumns = preparedStatement.getMetaData().getColumnCount();
            this.numberOfRows = data.getInt(1);
            this.numberOfPages = (int) Math.ceil((double) this.numberOfRows / PAGE_SIZE);
        } catch (SQLException throwable) {
            Tracer.get().logError(this.getClass(), throwable, "Error in setting current table");
            throw new IllegalStateException(throwable.getMessage());
        }

        Tracer.get().logInfo(this.getClass(), "Current table has been set: ".concat(tableName));
        Tracer.get().logInfo(this.getClass(), "SQL query has been prepared: ".concat(sqlQuery));
    }

    @Override
    public String getCurrentTable() {
        getPreparedStatement();
        return tableName;
    }

    @Override
    public List<ColumnMetadata> getCurrentTableColumnTypes(FieldMetadataFabric metaFabric) {
        if (metaFabric == null) {
            IllegalArgumentException throwable = new IllegalArgumentException("Field metadata fabric reference " +
                    "param must be not null");
            Tracer.get().logError(this.getClass(), throwable, "Error in getting metadata fabric instance");
            throw throwable;
        }
        List<ColumnMetadata> res = new ArrayList<>();
        try {
            ResultSetMetaData rmd = getPreparedStatement().getMetaData();
            int columnNumber = rmd.getColumnCount();
            for (int i = 1; i <= columnNumber; i++) {
                String columnName = rmd.getColumnName(i);
                int columnJdbcType = rmd.getColumnType(i);
                res.add(metaFabric.getMetadata(columnJdbcType, columnName));
            }
        } catch (SQLException throwable) {
            String msg = String.format("Error in getting the metadata of table \"%s\"", tableName);
            Tracer.get().logError(this.getClass(), throwable, msg);
        }

        Tracer.get().logInfo(this.getClass(),
                String.format("Determine columns of table \"%s\" (%d items): ", tableName, res.size())
                        .concat(
                                res
                                        .stream()
                                        .map(ColumnMetadata::toString)
                                        .collect(Collectors.joining(", "))
                        )
        );
        return res;
    }

    @Override
    public Object[][] getData(int pageId) {
        if (pageId < 0 || pageId > numberOfPages - 1) {
            IllegalArgumentException exception = new IllegalArgumentException(String.format(
                    "Page Id must be positive number, which not exceeds %d", numberOfPages));
            Tracer.get().logError(this.getClass(), exception, String.format("Wrong param passed: %d", pageId));
            throw exception;
        }
        Object[][] res = new Object[PAGE_SIZE][];
        try {
            PreparedStatement pstmt = getPreparedStatement();
            pstmt.setInt(1, pageId * PAGE_SIZE);
            ResultSet queryData = pstmt.executeQuery();

            int rowId = -1;
            while (queryData.next()) {
                rowId++;
                Object[] rowData = new Object[numberOfColumns];
                for (int colId = 0; colId < numberOfColumns; colId++) {
                    rowData[colId] = queryData.getObject(colId + 1);
                }
                res[rowId] = rowData;
            }
        } catch (SQLException throwable) {
            Tracer.get().logError(this.getClass(), throwable, "Error in getting list of tables");
        }

        Tracer.get().logInfo(this.getClass(),
                String.format("Retrieve page #%d (%d rows) from table %s", pageId, PAGE_SIZE, tableName)
        );
        return res;
    }

    @Override
    public int getRowCount() {
        getPreparedStatement(); //проверка, что контекст БД инициализирован
        Tracer.get().logInfo(this.getClass(),
                String.format("Determine number of rows in table \"%s\": %d", tableName, numberOfRows)
        );
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        getPreparedStatement(); //проверка, что контекст БД инициализирован
        Tracer.get().logInfo(this.getClass(),
                String.format("Determine number of columns in table \"%s\": %d", tableName, numberOfColumns)
        );
        return numberOfColumns;
    }

    @Override
    public int getNumberOfPages() {
        getPreparedStatement();
        Tracer.get().logInfo(this.getClass(),
                String.format("Determine number of pages in table \"%s\": %d", tableName, numberOfPages)
        );
        return numberOfPages;
    }

    @Override
    public void close() throws IOException {
        try{
            this.disconnect();
        } catch (SQLException ex) {
            Tracer.get().logError(this.getClass(), ex, "Error in closing db connection");
        }
    }
}
