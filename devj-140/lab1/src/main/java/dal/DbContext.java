package dal;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbContext implements AutoCloseable{
    private static final String DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String CONNECTION_STR = "jdbc:derby:derbyDB;create=true";

    private static Connection connectionObj;
    private DbContext(){};

    public static Connection get(){
        if (connectionObj == null){
            try {
                Class.forName(DRIVER_NAME);
                connectionObj = DriverManager.getConnection(CONNECTION_STR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connectionObj;
    }

    @Override
    public void close() throws Exception {
        if (connectionObj!=null){
            connectionObj.close();
        }
    }
}
