package utils;

import contracts.PropertiesRepo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppProperties implements PropertiesRepo {
    private final Properties props;
    private static final String PROPERTIES_FILE_NAME = "app.properties";
    private static PropertiesRepo instance;

    private AppProperties() throws IOException {
        String fileName = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + PROPERTIES_FILE_NAME;
        this.props = new Properties();
        try(FileInputStream fis = new FileInputStream(fileName)) {
            this.props.load(fis);
        }catch (IOException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage());
        }
    }

    public static PropertiesRepo getInstance() throws IOException {
        if (instance == null){
            instance = new AppProperties();
        }
        return instance;
    }

    @Override
    public String getValue(String key){
        return this.props.getProperty(key);
    }
}
