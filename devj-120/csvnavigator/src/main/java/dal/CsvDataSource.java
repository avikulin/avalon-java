package dal;

import contracts.CachedDataSource;
import contracts.DiagnosticLogger;
import contracts.SourceRepo;
import contracts.Tokenizer;
import utils.Tracer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static constants.Constants.*;

public class CsvDataSource implements CachedDataSource {
    private File csvFile;
    private final SourceRepo fileSource;
    private final Tokenizer tokenizer;
    private List<Object[]> dataStore;
    private List<String> columnNames;
    private DiagnosticLogger logger;
    private boolean hasHeaderRow;
    private String columnNameTemplate;
    private int numberOfRows;
    private int numberOfColumns;
    private int firstRowId;
    private String tableName;
    private boolean hasDataLoaded;

    public CsvDataSource(SourceRepo fileSource, Tokenizer tokenizer) {
        if (fileSource == null || tokenizer == null) {
            throw new NullPointerException("File source and tokenizer reference params are mandatory");
        }
        this.dataStore = new ArrayList<>();
        this.columnNames = new ArrayList<>();
        this.hasDataLoaded = false;
        this.fileSource = fileSource;
        this.tokenizer = tokenizer;
        this.logger = Tracer.get();
        this.numberOfRows = 0;
        this.numberOfColumns = 0;
        this.firstRowId = 0;
        this.columnNameTemplate = DEFAULT_COLUMN_NAME_TEMPLATE;
        this.hasHeaderRow = DEFAULT_HAS_HEADER_ROW;

        logger.logInfo(this.getClass(), "Instance has been created");
    }

    private void checkDataState() {
        if (!hasDataLoaded) {
            String msg = "Data context can't be modified because data " +
                    "wasn't previously loaded from CSV-file";
            IllegalStateException e = new IllegalStateException(msg);
            logger.logError(this.getClass(), e, "Invalid data state", msg);
            throw e;
        }
    }

    private void checkRowAndColumnIndexes(int rowId, int columnId) {
        if (rowId < 0 || rowId > numberOfRows - 1 || columnId < 0 || columnId > numberOfColumns - 1) {
            String msg = String.format("Invalid parameters (row = %d, column = %d): " +
                    "row idx must be between 0 and %d, " +
                    "and column id must be between 0 and %d", rowId, columnId, numberOfRows - 1, numberOfColumns - 1);
            IndexOutOfBoundsException e = new IndexOutOfBoundsException(msg);
            logger.logError(this.getClass(), e, "Error in getting data from cache", msg);
            throw e;
        }
    }

    @Override
    public void readDataFromFile(File file) throws Exception {
        if (file == null) {
            String msg = "File reference must be not null";
            NullPointerException e = new NullPointerException(msg);
            logger.logError(this.getClass(), e, "Missing source file param", msg);
            throw e;
        }
        try {
            logger.logInfo(this.getClass(), "Loading file ".concat(file.getAbsolutePath()));

            this.reset();
            this.csvFile = file;
            fileSource.loadFile(this.csvFile);

            int actualNumberOfColumns = 0;
            for (String s : fileSource) {
                if (s != null && !s.isEmpty()) {
                    Object[] tokens = tokenizer.tokenize(s);
                    actualNumberOfColumns = Math.max(actualNumberOfColumns, tokens.length);
                    dataStore.add(tokens);
                }
            }

            this.numberOfRows = dataStore.size();
            this.numberOfColumns = actualNumberOfColumns;

            if (hasHeaderRow) {
                this.firstRowId = 1;
                this.numberOfRows--;
                this.columnNames = Arrays.stream(dataStore.get(0)).map(Object::toString).collect(Collectors.toList());
            }

            int numberOfColumnsInFirstRow = columnNames.size();
            for (int i = numberOfColumnsInFirstRow; i < actualNumberOfColumns; i++) {
                columnNames.add(String.format(this.columnNameTemplate, i));
            }

            this.tableName = fileSource.getFileName();
            this.fileSource.close();

            if (numberOfRows < 1) {
                String msg = "Source file has no data rows";
                IllegalStateException e = new IllegalStateException(msg);
                logger.logError(this.getClass(), e, "No data found in file", msg);
                throw e;
            }

            this.hasDataLoaded = true;
        } catch (Exception e) {
            String msg = "Error in loading CSV-file procedure: ".concat(e.getMessage());
            logger.logError(this.getClass(), e, msg);
            throw new IllegalStateException(msg);
        }

        logger.logInfo(this.getClass(), "Data has been loaded to cache", this.toString());
    }

    @Override
    public void reloadDataFromFile() throws Exception {
        checkDataState();
        logger.logInfo(
                this.getClass(),
                "Reloading current file: "
                        .concat(this.csvFile.getAbsolutePath())
        );
        readDataFromFile(this.csvFile);
    }

    @Override
    public void setHeaderRowPresent(boolean headerRowPresent) {
        this.hasHeaderRow = headerRowPresent;
    }

    @Override
    public boolean isHeaderRowPresent() {
        return this.hasHeaderRow;
    }

    @Override
    public boolean isReady() {
        return hasDataLoaded;
    }

    @Override
    public String getTableName() {
        checkDataState();
        return this.tableName;
    }

    @Override
    public void setColumnNameTemplate(String template) {
        if (template == null || template.isEmpty()) {
            String msg = "Template string must be not-null and non-empty";
            IllegalArgumentException e = new IllegalArgumentException(msg);
            logger.logError(this.getClass(), e, "Error in setting column name template", msg);
            throw e;
        }
        this.columnNameTemplate = template;
    }

    @Override
    public String getColumnNamesTemplate() {
        return this.columnNameTemplate;
    }

    @Override
    public Object getData(int rowId, int columnId) {
        checkDataState();
        checkRowAndColumnIndexes(rowId, columnId);
        Object[] rowData = dataStore.get(rowId + firstRowId);
        return (columnId < rowData.length) ? rowData[columnId] : DEFAULT_CELL_VALUE;
    }

    @Override
    public String getColumnName(int columnId) {
        checkDataState();
        checkRowAndColumnIndexes(0, columnId);
        return this.columnNames.get(columnId);
    }

    @Override
    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    @Override
    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    @Override
    public void reset() {
        this.dataStore.clear();
        this.columnNames.clear();
        this.hasDataLoaded = false;
        this.csvFile = null;
        this.tableName = "";
        this.numberOfRows = 0;
        this.numberOfColumns = 0;
        this.firstRowId = 0;

        logger.logInfo(this.getClass(), "State has been reset: ".concat(this.toString()));
    }

    @Override
    public String toString() {
        return "CsvDataSource{" +
                "csvFile=" + (csvFile == null ? "null" : csvFile.getAbsolutePath()) +
                ", fileSource (current file)=" + (fileSource == null ? "null" : fileSource.getFileName()) +
                ", tokenizer (current delimiter)=" + (tokenizer == null ? "null" :
                                                        "'" + tokenizer.getCurrentDelimiter()) + "'" +
                ", dataStore (size)=" + (dataStore == null ? "null" : dataStore.size()) +
                ", columnNames=" + (columnNames == null ? "null" : String.join(", ", columnNames)) +
                ", hasHeaderRow=" + hasHeaderRow +
                ", columnNameTemplate='" + columnNameTemplate + '\'' +
                ", numberOfRows=" + numberOfRows +
                ", numberOfColumns=" + numberOfColumns +
                ", firstRowId=" + firstRowId +
                ", tableName='" + tableName + '\'' +
                ", hasDataLoaded=" + hasDataLoaded +
                '}';
    }
}
