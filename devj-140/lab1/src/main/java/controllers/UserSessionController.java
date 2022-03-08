package controllers;

import dto.User;
import exceptions.UserException;

import java.io.IOException;

/**
 *
 * @author denis
 */
public class UserSessionController {
    private static UserSessionController instance;
    private User user;

    private UserSessionController(){}

    public static UserSessionController getInstance(){
        if (instance == null){
            instance = new UserSessionController();
        }
        return instance;
    }
    public boolean createSession(String name, String pass) throws UserException, IOException {
        if (name.isEmpty() || pass.isEmpty()) throw new UserException("Имя и пароль не должны быть пустыми");
        String regex = "A-Za-z0-9!";
        if(pass.replaceAll("[" + regex + "]", "").length()>0) {
            throw new UserException("Пароль может содержать символы: " + regex);
        }
        this.user = new User(name, pass);
        return true;
    }
    
    public User getUser(){
        return user;
    }
}
