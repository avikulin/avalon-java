package dto;

import interfaces.metadata.ColumnMetadata;

public class FieldTypeDescriptor implements ColumnMetadata {
    private String fieldName;
    private Class<?> type;

    public FieldTypeDescriptor(String fieldName, Class<?> type) {
        if (fieldName == null || type == null) {
            throw new IllegalArgumentException("All param references must be not null");
        }

        this.fieldName = fieldName;
        this.type = type;
    }

    @Override
    public String getName() {
        return fieldName;
    }

    @Override
    public Class<?> getDataType() {
        return type;
    }

    @Override
    public String toString() {
        return "{fieldName='" + fieldName + "', type=" + type + '}';
    }
}
