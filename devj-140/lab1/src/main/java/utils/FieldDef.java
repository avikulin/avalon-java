package utils;

public class FieldDef {
    private final String sqlType;
    private final boolean isPK;
    private final String classFieldName;
    private final Class<?> classFieldType;

    public FieldDef(String classFieldName, Class<?> classFieldType, String sqlType, boolean isPK) {
        if (classFieldName == null || classFieldName.isEmpty()){
            throw new IllegalArgumentException("Field name object reference must be not-null & non-empty");
        }
        if (classFieldType == null){
            throw new IllegalArgumentException("Field type object reference must be not-null & non-empty");
        }
        if (sqlType == null || sqlType.isEmpty()){
            throw new IllegalArgumentException("Sql type name must be not-null & non-empty");
        }
        this.classFieldName = classFieldName;
        this.classFieldType = classFieldType;
        this.sqlType = sqlType;
        this.isPK = isPK;
    }

    public String getClassFieldName() {
        return classFieldName;
    }

    public Class<?> getClassFieldType() {
        return classFieldType;
    }

    public String getSqlType() {
        return sqlType;
    }

    public boolean isPK() {
        return isPK;
    }
}
