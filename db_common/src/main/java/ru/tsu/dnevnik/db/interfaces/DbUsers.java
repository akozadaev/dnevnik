package ru.tsu.dnevnik.db.interfaces;

import ru.tsu.dnevnik.db.models.UserUI;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Alexey on 04.01.2015.
 */
@Remote
public interface DbUsers {
    UserUI getUserUI(String login);
    List<UserUI> getAllUsers();
    int createUser(String login, String last_name, String first_name, String middle_name, int password_hash, int role, int status);


}
