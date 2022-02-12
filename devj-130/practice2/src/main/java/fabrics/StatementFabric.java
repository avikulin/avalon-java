package fabrics;

import annotations.DataClass;
import annotations.DataField;
import annotations.Mandatory;
import annotations.PrimaryKey;
import dto.FieldDescriptor;
import enums.FieldSubSet;
import enums.Mode;
import enums.ProjectionType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StatementFabric {
    private static final String SQL_TEMPLATE_INSERT = "insert into \"%s\" (%s) values (%s)";
    private static final String SQL_TEMPLATE_SELECT = "select %s from \"%s\" where %s";
    private static final String SQL_TEMPLATE_UPDATE = "update \"%s\" set %s where %s";
    private static final String SQL_TEMPLATE_DELETE = "delete from \"%s\" where %s";

    private final Object dataObj;
    private final String tableName;
    private final List<FieldDescriptor> allFields;
    private final Map<FieldDescriptor, Object> dataFields;

    private Mode mode;

    private StatementFabric(Object dataObj, Mode initialMode){
        if (dataObj == null) {
            throw new IllegalArgumentException("Class reference must be not-null");
        }
        Class<?> objType = dataObj.getClass();
        DataClass dcAnnotation = objType.getAnnotation(DataClass.class);
        if (dcAnnotation == null) {
            throw new IllegalArgumentException("Class of the object reference passed doesn't correspond to @DataClass");
        }
        this.dataObj = dataObj;
        this.tableName = dcAnnotation.tableName();
        this.dataFields = new TreeMap<>();
        this.allFields = new ArrayList<>();
        this.mode = initialMode;
    }
    public StatementFabric(Object dataObj) {
        this(dataObj, Mode.INSERT_OR_UPDATE);
        extractMetaData(dataObj.getClass());
    }

    /*
        select *
        from [searchedEntity] inner join [searchKey.getClass()] on [joinExpression]
        where [searchKey.getClass()]."..." = [searchKey]."..."
     */
    public StatementFabric(Class<?> searchedEntity, Object searchKey, String joinExpression){
        this(searchKey, Mode.SELECT_ONLY);
        extractMetaData(searchKey.getClass());
    }

    //---select * from [searchedEntity] where [fieldGetter] like [pattern]
    public StatementFabric(Class<?> searchedEntity, Method fieldGetter, String pattern){

    }

    public void extractMetaData(Class<?>objType) {
        for (Method method : objType.getDeclaredMethods()) {
            DataField dfgAnnotation = method.getAnnotation(DataField.class);
            if (dfgAnnotation != null) {
                String fieldName = dfgAnnotation.fieldName();
                int fieldOrder = dfgAnnotation.order();
                Class<?> returnType = method.getReturnType();
                PrimaryKey pkAnnotation = method.getAnnotation(PrimaryKey.class);
                boolean isPK = pkAnnotation != null;

                Mandatory mfAnnotation = method.getAnnotation(Mandatory.class);
                boolean isMandatory = mfAnnotation !=null || isPK;

                FieldDescriptor field = new FieldDescriptor(fieldName, returnType, fieldOrder, isPK, isMandatory);
                this.allFields.add(field);
                try {
                    Object value = method.invoke(dataObj);
                    if (value == null && isPK){
                        throw new IllegalStateException("Null value returned by @PrimaryKey annotated method");
                    }
                    if (this.mode == Mode.INSERT_OR_UPDATE && value == null && isMandatory){
                        this.mode = Mode.UPDATE_ONLY;
                    }

                    if (value != null) {
                        this.dataFields.put(field, value);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(
                            String.format(
                                    "Can't get value of the attribute %s\\.%s",
                                    tableName,
                                    fieldName
                            )
                    );
                }
            }
        }
        this.allFields.sort(Comparator.naturalOrder());
    }

    private String getDataFieldExpression(FieldSubSet filter, String suffix, String delimiter, boolean extendedFormat) {
        Function<FieldDescriptor, String> funcProject = extendedFormat ?
                (field -> String.format("\"%s\".%s%s", this.tableName, field.getName(), suffix)) :
                (field -> String.format("%s%s", field.getName(), suffix));

        Predicate<FieldDescriptor> funcPredicate = fieldDescriptor -> {
            switch (filter) {
                case PK_FIELDS:
                    return fieldDescriptor.isPrimaryKey();
                case DATA_FIELDS:
                    return !fieldDescriptor.isPrimaryKey();
                default:
                    return true;
            }
        };

        Collection<FieldDescriptor> obj = (filter == FieldSubSet.ALL_FIELDS) ? this.allFields : this.dataFields.keySet();
        String res = obj
                .stream()
                .filter(funcPredicate)
                .map(funcProject)
                .collect(Collectors.joining(delimiter));

        if (res.isEmpty()) {
            throw new IllegalArgumentException(
                    "Empty of wrong @DataField and @PrimaryKey annotation formatting in class "
                            .concat(dataObj.getClass().getCanonicalName())
            );
        }

        return res;
    }

    public String getTableExpression() {
        return this.tableName;
    }

    String getProjectionExpression(ProjectionType type, boolean includeTableRef) {
        FieldSubSet filter = (type == ProjectionType.IN) ? FieldSubSet.PK_AND_DATA_FIELDS : FieldSubSet.ALL_FIELDS;
        return getDataFieldExpression(filter, "", ", ", includeTableRef);
    }

    public String getAssignmentExpression(boolean includeTableRef) {
        return getDataFieldExpression(FieldSubSet.DATA_FIELDS, "=?", ", ", includeTableRef);
    }

    public String getFilterPredicate(FieldSubSet filter) {
        return getDataFieldExpression(filter, "=?", " AND ", true);
    }

    public String getDataFieldValues() {
        return this.dataFields.entrySet()
                .stream()
                .map(entry -> "?")
                .collect(Collectors.joining(", "));
    }

    public String getSelectStatement() {
        return String.format(
                SQL_TEMPLATE_SELECT,
                getProjectionExpression(ProjectionType.OUT, true),
                getTableExpression(),
                getFilterPredicate(FieldSubSet.PK_AND_DATA_FIELDS)
        );
    }

    public String getInsertStatement() {
        return String.format(
                SQL_TEMPLATE_INSERT,
                getTableExpression(),
                getProjectionExpression(ProjectionType.IN, false),
                getDataFieldValues()
        );
    }

    public String getUpdateStatement() {
        return String.format(
                SQL_TEMPLATE_UPDATE,
                getTableExpression(),
                getAssignmentExpression(true),
                getFilterPredicate(FieldSubSet.PK_FIELDS)
        );
    }

    public String getDeleteStatement() {
        return String.format(
                SQL_TEMPLATE_DELETE,
                getTableExpression(),
                getFilterPredicate(FieldSubSet.PK_FIELDS)
        );
    }

    public Mode getMode() {
        return mode;
    }
}
