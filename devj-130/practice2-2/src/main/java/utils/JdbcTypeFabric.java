package utils;

import dto.FieldTypeDescriptor;
import interfaces.metadata.ColumnMetadata;
import interfaces.metadata.FieldMetadataFabric;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JdbcTypeFabric implements FieldMetadataFabric {
    private static Map<Integer, Class<?>> typesMap;
    private static JdbcTypeFabric obj;

    private JdbcTypeFabric() {
        init();
    }

    public static JdbcTypeFabric get() {
        if (obj == null) {
            obj = new JdbcTypeFabric();
            Tracer.get().logInfo(JdbcTypeFabric.class, "Instance created");
        }
        return obj;
    }

    private static void init() {
        typesMap = new HashMap<>();
        typesMap.put(Types.ARRAY, Array.class);
        typesMap.put(Types.BIGINT, long.class);
        typesMap.put(Types.BINARY, byte[].class);
        typesMap.put(Types.VARBINARY, byte[].class);
        typesMap.put(Types.LONGVARBINARY, InputStream.class);
        typesMap.put(Types.BIT, boolean.class);
        typesMap.put(Types.BOOLEAN, boolean.class);
        typesMap.put(Types.CHAR, char.class);
        typesMap.put(Types.NCHAR, char.class);
        typesMap.put(Types.VARCHAR, String.class);
        typesMap.put(Types.NVARCHAR, String.class);
        typesMap.put(Types.LONGVARCHAR, Reader.class);
        typesMap.put(Types.LONGNVARCHAR, Reader.class);
        typesMap.put(Types.DATE, Date.class);
        typesMap.put(Types.TIME, Time.class);
        typesMap.put(Types.TIME_WITH_TIMEZONE, Time.class);
        typesMap.put(Types.TIMESTAMP, Date.class);
        typesMap.put(Types.TIMESTAMP_WITH_TIMEZONE, Date.class);
        typesMap.put(Types.DECIMAL, BigDecimal.class);
        typesMap.put(Types.NUMERIC, BigDecimal.class);
        typesMap.put(Types.DOUBLE, double.class);
        typesMap.put(Types.FLOAT, float.class);
        typesMap.put(Types.REAL, float.class);
        typesMap.put(Types.INTEGER, int.class);
        typesMap.put(Types.SMALLINT, int.class);
        typesMap.put(Types.BLOB, Blob.class);
        typesMap.put(Types.CLOB, Clob.class);
        typesMap.put(Types.NCLOB, Clob.class);
        typesMap.put(Types.SQLXML, String.class);
        typesMap.put(Types.JAVA_OBJECT, byte[].class);
    }

    @Override
    public ColumnMetadata getMetadata(Integer sqlTypeID, String fieldName) throws IllegalStateException {
        Class<?> type = typesMap.get(sqlTypeID);
        if (type == null) {
            String msg = "No such type registered";
            IllegalStateException err = new IllegalStateException(msg);
            Tracer.get().logError(this.getClass(), err,
                    "Error in getType() method with SQL-type ID passed = ".concat(String.valueOf(sqlTypeID)));
            throw err;
        }
        return new FieldTypeDescriptor(fieldName, type);
    }
}
