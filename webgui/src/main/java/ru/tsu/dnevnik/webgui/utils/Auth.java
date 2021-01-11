package ru.tsu.dnevnik.webgui.utils;

import com.vaadin.server.VaadinSession;
import ru.tsu.dnevnik.db.interfaces.DbUsers;
import ru.tsu.dnevnik.db.models.UserUI;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by Alexey on 15.01.2015.
 */
public class Auth {

    private static UserUI userUI;
    private static DataSource dataSource;
    private DbUsers dbUsers;
    public final static String PARAM_SESSION_LOGIN = "login";

    public static UserUI login(String login, Integer passwordHash, VaadinSession session, DbUsers dbUsers)
            throws NamingException {
        userUI = dbUsers.getUserUI(login);
        System.out.println("Где USER? " + userUI.toString());
        if (authorization(passwordHash, userUI.password_hash)) {

            session.setAttribute(PARAM_SESSION_LOGIN, login);
            System.out.println("USER все хорошо " + userUI.toString());
            return userUI;
        } else return null;
    }

    public static String getLogin(VaadinSession session) {
        return (String)session.getAttribute(PARAM_SESSION_LOGIN);
    }

    private static boolean authorization(int inputPass, int dbPass) {
        System.out.println("Сравним пароли " + inputPass +" = " + dbPass);
        return (inputPass == dbPass);
    }

    public static void logout(VaadinSession session) {
        session.setAttribute(PARAM_SESSION_LOGIN, null);
//        session.close();
    }
}
