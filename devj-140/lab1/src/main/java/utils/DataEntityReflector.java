package utils;

import contracts.Reflector;
import javafx.beans.NamedArg;
import tags.DataEntityConstructor;
import tags.DataField;
import tags.PKey;
import tags.TableName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class DataEntityReflector<T> implements Reflector<T>, Iterable<Map.Entry<String, FieldDef>> {
    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final List<String> constructorParams;
    private final LinkedHashMap<String, FieldDef> fields;

    public DataEntityReflector(Class<T> clazz) {
        if (clazz == null){
            throw new NullPointerException("Class object reference must be not null");
        }
        this.clazz = clazz;
        this.constructorParams = new ArrayList<>();
        try {
            this.constructor = this.clazz.getConstructor(DataEntityConstructor.class);
            for (Parameter p : this.constructor.getParameters()){
                NamedArg name = p.getAnnotation(NamedArg.class);
                this.constructorParams.add(name.value());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Class passed does not have properly " +
                    "marked by @DataEntityConstructor tag");
        }

        this.fields = new LinkedHashMap<>();
        for (Field f: clazz.getDeclaredFields()){
            DataField[] fieldMeta = f.getAnnotationsByType(DataField.class);
            if (fieldMeta.length == 0){
                continue;
            }

            String classFieldName = f.getName();
            Class<?> classFieldType = f.getType();
            String dbFieldName = fieldMeta[0].name();
            String dbFieldType = fieldMeta[0].slqType();

            if (dbFieldName.isEmpty() || dbFieldType.isEmpty()){
                throw new IllegalStateException(String.format("Illegal declaration of the field \"%s\"", f.getName()));
            }

            PKey[] primaryKeyMeta = f.getAnnotationsByType(PKey.class);
            boolean isPK = primaryKeyMeta.length > 0;

            this.fields.put(dbFieldName, new FieldDef(classFieldName, classFieldType, dbFieldType, isPK));
        }
    }

    @Override
    public String getTableName(){
        TableName tnTag = clazz.getAnnotation(TableName.class);
        if (tnTag == null || tnTag.name().isEmpty()){{
            throw new IllegalArgumentException("Class passed is not properly marked by @TableName tag");
        }}
        return tnTag.name();
    }

    @Override
    public Object[] extractValues(T item) {
        Object[] res = new Object[this.constructorParams.size()];
        int pointer = -1;
        for (String fn : this.constructorParams){
            String classFieldName = this.fields.get(fn).getClassFieldName();
            try {
                Field f = this.clazz.getDeclaredField(classFieldName);
                pointer++;
                res[pointer] = f.get(item);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                throw new IllegalStateException("Corrupted state of the field dictionary");
            }
        }
        return res;
    }

    @Override
    public T createInstance(Object... params) {
        T instance = null;
        try {
            instance = this.constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal parameter sequence");
        }
        return instance;
    }

    @Override
    public List<String> getFieldNames(){
        return this.constructorParams.stream().map(String::new).collect(Collectors.toList()); //deep copy
    }

    @Override
    public Iterator<Map.Entry<String, FieldDef>> iterator() {
        return this.fields.entrySet().iterator();
    }
}
