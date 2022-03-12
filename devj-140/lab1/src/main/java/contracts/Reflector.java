package contracts;

import utils.FieldDef;

import java.util.List;
import java.util.Map;

public interface Reflector<T> extends Iterable<Map.Entry<String, FieldDef>> {
    String getTableName();
    List<String> getFieldNames();
    Object[] extractValues(T item);
    T createInstance(Object... params);
}
