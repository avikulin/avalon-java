package dto;

import contracts.PropertiesRepo;
import exceptions.UserException;
import utils.AppProperties;

import java.io.IOException;

public class User {
    
    private final String name;
    private final String password;

    private static final String PROPERTY_USER = "user";
    private static final String PROPERTY_PASSWORD = "password";

    public User(String name, String password) throws IOException, UserException {
        validate(name, password);
        this.name = name;
        this.password = password;
    }

    private void validate(String name, String password) throws IOException, UserException {
        if (name == null || name.isEmpty()){
            throw new UserException("User name must be not-null & non-empty string");
        }
        if (password == null || password.isEmpty()){
            throw new UserException("Password must be not-null & non-empty string");
        }

        PropertiesRepo props = AppProperties.getInstance();
        if (!name.equals(props.getValue(PROPERTY_USER))||
                !password.equals(props.getValue(PROPERTY_PASSWORD))){
            throw new UserException("Login or password are incorrect");
        }
    }
}
