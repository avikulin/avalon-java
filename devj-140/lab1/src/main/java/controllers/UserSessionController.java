package controllers;

import dto.User;
import exceptions.UserException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 *
 * @author denis
 */
public class UserSessionController {
    private static final String NAME_ALLOWED_SYMBOLS = "^[A-Za-z0-9_]+$";
    private static final String PWD_ALLOWED_SYMBOLS = "^[A-Za-z0-9!$#]+$";
    private static UserSessionController instance;
    private User user;
    private final Pattern nameCheckExpr;
    private final Pattern pwdCheckExpr;

    private UserSessionController(){
        this.nameCheckExpr = Pattern.compile(NAME_ALLOWED_SYMBOLS);
        this.pwdCheckExpr = Pattern.compile(PWD_ALLOWED_SYMBOLS);
    }

    public static UserSessionController getInstance(){
        if (instance == null){
            instance = new UserSessionController();
        }
        return instance;
    }

    public boolean createSession(String name, String pass) throws UserException, IOException {
        if (name.isEmpty() || pass.isEmpty()) {
            throw new UserException("Name and password must be non-empty");
        }

        if (!nameCheckExpr.matcher(name).matches()){
            throw new UserException(
                    String.format("Name must consist of (%s)", NAME_ALLOWED_SYMBOLS)
            );
        }

        if (!pwdCheckExpr.matcher(pass).matches()){
            throw new UserException(
                    String.format("Password must consist of (%s)", PWD_ALLOWED_SYMBOLS)
            );
        }

        this.user = new User(name, pass);
        return true;
    }
    
    public User getUser(){
        return user;
    }
}
