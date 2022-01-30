package interfaces.metadata;

import java.util.function.Function;

public interface ColumnMetadata {
    String getName();
    Class<?> getDataType();
    //JdbcAccessorFunc<Integer, Object> getterFunc(Integer columnId);
}
