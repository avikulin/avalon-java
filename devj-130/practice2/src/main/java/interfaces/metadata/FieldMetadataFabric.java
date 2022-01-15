package interfaces.metadata;

public interface FieldMetadataFabric {
    ColumnMetadata getMetadata(Integer sqlTypeID, String fieldName) throws IllegalStateException;
}
