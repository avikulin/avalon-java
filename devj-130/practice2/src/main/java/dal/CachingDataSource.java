package dal;

import interfaces.metadata.ColumnMetadata;
import interfaces.dal.DataCache;
import interfaces.dal.DbConnection;
import interfaces.metadata.FieldMetadataFabric;
import utils.JdbcTypeFabric;
import utils.Tracer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static constants.Constants.*;

/*
    Постраничное кэширование. Пример:
    Глубина кэширования - 3 страницы (нумерация с нуля)
    Размер страницы - 3 строки.

    Строка #1 - страница 0 = (1-1)//3 = 0
    Строка #2 - страница 0 = (2-1)//3 = 0
    Строка #3 - страница 0 = (3-1)//3 = 0

    Строка #4 - страница 1 = (4-1)//3 = 1
    Строка #5 - страница 1 = (5-1)//3 = 1
    Строка #6 - страница 1 = (6-1)//3 = 1

    Строка #7 - страница 2 = (7-1)//3 = 2
    Строка #8 - страница 2 = (8-1)//3 = 2
    Строка #9 - страница 2 = (9-1)//3 = 2

    Номер странцы = (номер строки - 1) // размер страницы

    Структура кэша: непрерывная подспоследовательность из 3-х страниц одинакового размера
        {
            first : Object[PAGE_SIZE][numberOfColumns]
            center: Object[PAGE_SIZE][numberOfColumns]
            last  : Object[PAGE_SIZE][numberOfColumns]
        }

    Вращение буфера путем вымещения страниц:
        при запросе строки определяется ее номер страницы
        если страница есть в кэше - возращается запрошенное значение
        если страницы нет в кэше - выполняется "вращение":
                                                       а) в центральную позицию буфера зачитывается страница с
                                                             отсутствующей в кэше строкой
                                                        б) в первую позицию буфера зачитывается предыдущая страница,
                                                            если она есть (номер страницы > -1)
                                                        в) в третью позицию буфера зачитывается последующая страница,
                                                            если она есть (номер страницы < getNumberOfPages()+1)
    -непрерывная подпоследовательность номеров странци: (2,3,4) + 6 = (5,6,7)

* */
public class CachingDataSource implements DataCache {
    private DbConnection dataContext;
    private final Object[][][] dataStore;
    private final int[] pageIndex;
    private final int numberOfRows;
    private final int numberOfColumns;
    private final int numberOfPages;

    public CachingDataSource(DbConnection dataContext) {
        if (dataContext == null || !dataContext.isReady()) {
            IllegalArgumentException ex = new IllegalArgumentException(
                    "Database connection reference param must be not null and database connection must be initialized");
            Tracer.get().logError(this.getClass(), ex, "Error in creating instance");
            throw ex;
        }

        this.dataContext = dataContext;

        this.dataStore = new Object[CACHE_DEPTH_IN_PAGES][PAGE_SIZE][];
        this.pageIndex = new int[CACHE_DEPTH_IN_PAGES];
        Arrays.fill(pageIndex, NOT_FOUND);

        this.numberOfRows = this.dataContext.getRowCount();
        this.numberOfColumns = this.dataContext.getColumnCount();
        this.numberOfPages = this.dataContext.getNumberOfPages();
        Tracer.get().logInfo(this.getClass(), "Instance created");
    }

    private void checkRowId(int rowID) {
        if (rowID > numberOfRows - 1 || rowID < 0) {
            IllegalStateException ex = new IllegalStateException(
                    String.format("Row number must be between %d and %d", 1, numberOfRows));
            Tracer.get().logError(this.getClass(), ex, "Incorrect rowID passed");
        }
    }

    private void checkColumnId(int columnID) {
        if (columnID > numberOfColumns - 1 || columnID < 0) {
            IllegalStateException ex = new IllegalStateException(
                    String.format("Column number must be between %d and %d", 1, numberOfColumns));
            Tracer.get().logError(this.getClass(), ex, "Incorrect columnID passed");
        }
    }

    @Override
    public void fillCache(int centerPageID) {
        int prevPageID = centerPageID - 1 > 0 ? centerPageID - 1 : NOT_FOUND;
        int nextPageID = centerPageID + 1 < numberOfPages ? centerPageID + 1 : NOT_FOUND;

        dataStore[1] = dataContext.getData(centerPageID); // центральная страница перезаписывается всегда
        pageIndex[1] = centerPageID;

        // прокрутка на 2 страницы вниз
        if (prevPageID == pageIndex[2] && pageIndex[2] != NOT_FOUND) {
            pageIndex[0] = pageIndex[2];
            dataStore[0] = dataStore[2];
        } else {
            pageIndex[0] = prevPageID;
            if (prevPageID != NOT_FOUND) {
                dataStore[0] = dataContext.getData(prevPageID);
            } else {
                dataStore[0] = null;
            }
        }

        // прокрутка на 2 страницы вверх
        if (nextPageID == pageIndex[0] && pageIndex[0] != NOT_FOUND) {
            pageIndex[2] = pageIndex[0];
            dataStore[2] = dataStore[0];
        } else {
            pageIndex[2] = nextPageID;
            if (nextPageID != NOT_FOUND) {
                dataStore[2] = dataContext.getData(nextPageID);
            } else {
                dataStore[2] = null;
            }
        }

        Tracer.get().logInfo(this.getClass(), "Cache has been filled with pages (\"-1\"=no data): "
                .concat(
                        Arrays.stream(pageIndex)
                                .mapToObj(Integer::toString)
                                .collect(Collectors.joining(", "))
                )
        );
    }

    @Override
    public String getTableName() {
        return dataContext.getCurrentTable();
    }

    @Override
    public Object getData(int rowId, int columnId) {
        checkRowId(rowId);
        checkColumnId(columnId);

        int pageID = rowId / PAGE_SIZE;
        int rowOffset = rowId % PAGE_SIZE;

        int firstInCachePageId = pageIndex[0];
        int pageOffset = NOT_FOUND;

        /*
         * Возможные варианты:
         *  - в кэше нет ни одной страницы, в т.ч. и запрошенной
         *  - в кэше есть заполненные страницы (больше одной), но среди них нет запрошенной
         *  - все данные уместились в одну страницу, которую запрашивают
         * */

        pageOffset = pageID - firstInCachePageId;
        if (pageOffset < 0 || pageOffset > 2 || pageIndex[pageOffset] != pageID) {
            Tracer.get().logInfo(this.getClass(),
                    String.format("Row #%d not present in cache (pageId=%d, page offset in cache =%d). " +
                            "Filling cache for center page %d", rowId, pageID, pageOffset, pageID)
            );
            fillCache(pageID);
            pageOffset = 1;
        }

        return dataStore[pageOffset][rowOffset][columnId];
    }

    @Override
    public List<ColumnMetadata> getDataStructure() {
        Tracer.get().logInfo(this.getClass(), "Getting column structure...");
        FieldMetadataFabric metaFabric = JdbcTypeFabric.get();
        return dataContext.getCurrentTableColumnTypes(metaFabric);
    }

    @Override
    public int getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public int getNumberOfColumns() {
        return numberOfColumns;
    }
}
