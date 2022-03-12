package dal;

import contracts.Extractable;
import contracts.Reflector;
import contracts.SqlFabric;
import utils.FieldDef;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class SqlFabricImpl<T extends Extractable> implements SqlFabric<T> {
    private final Reflector<T> reflector;

    public SqlFabricImpl(Reflector<T> reflector) {
        if (reflector == null){
            throw new IllegalArgumentException("Reflector reference must be set");
        }
        this.reflector = reflector;
    }

    @Override
    public void getAllRecords(Class<T> clazz, List<T> receiver) {
        if (clazz == null){
            throw new NullPointerException("Class object reference must be not null");
        }
        String fieldNames = String.join(", ", this.reflector.getFieldNames());
        String sqlStatement = String.format("SELECT %s FROM %s", fieldNames, reflector.getTableName());
        try(Connection connection = DbContext.get()){
            PreparedStatement pstm = connection.prepareStatement(sqlStatement);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()){
                int numberOfColumns = resultSet.getMetaData().getColumnCount();
                Object[] constructorParams = new Object[numberOfColumns];
                for (int i=1; i <=numberOfColumns; i++){
                    constructorParams[i] = resultSet.getObject(i);
                }
                T item = reflector.createInstance(constructorParams);
                receiver.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertRecord(T element){
        if (element == null){
            throw new NullPointerException("Inserted element object reference must be not null");
        }

        String tableName = reflector.getTableName();
        StringJoiner fieldNames = new StringJoiner(", ");
        StringJoiner fieldValues = new StringJoiner(", ");
        for (String fn : reflector.getFieldNames()){
            fieldNames.add(fn);
            fieldValues.add("?");
        }

        String sqlStatement = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, fieldNames, fieldValues);
        try(Connection connection = DbContext.get()){
            PreparedStatement pstm = connection.prepareStatement(sqlStatement);
            int pos = -1;
            for (Object o : reflector.extractValues(element)){
                pos++;
                pstm.setObject(pos, o);
            }
            pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRecord(T element) {
        if (element == null){
            throw new NullPointerException("Updated element object reference must be not null");
        }

        String tableName = reflector.getTableName();
        StringJoiner fieldNames = new StringJoiner(", ");
        StringJoiner fieldValues = new StringJoiner(", ");

        for (String fn : reflector.getFieldNames()){
            fieldNames.add(fn);
            fieldValues.add("?");
        }

        String sqlStatement = String.format("UPDATE %s SET %s WHERE %s", tableName, fieldNames, fieldValues);
    }

    @Override
    public void deleteRecord(T element) {
        /*return null;*/
    }

    @Override
    public void createTable(Class<T> clazz) {
        if (clazz == null){
            throw new NullPointerException("Class object reference must be not null");
        }

        StringBuilder res = new StringBuilder();
        res.append("CREATE TABLE ");
        res.append(reflector.getTableName());
        res.append(" (");

        StringJoiner fieldsSubExpr = new StringJoiner(", ");
        StringJoiner pkeysSubExpr = new StringJoiner(", ");

        for (Map.Entry<String,FieldDef> fd : this.reflector){
            String fieldDef = String.format("%s %s", fd.getKey(), fd.getValue().getSqlType());
            fieldsSubExpr.add(fieldDef);
            if (fd.getValue().isPK()){
                pkeysSubExpr.add(fd.getKey());
            }
        }

        fieldsSubExpr.add(String.format("PRIMARY KEY(%s)", pkeysSubExpr.toString()));
        res.append(fieldsSubExpr.toString());
        res.append(")");

        /*return res.toString();*/
    }
}
