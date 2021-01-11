package ru.tsu.dnevnik.db.interfaces;

import ru.tsu.dnevnik.db.models.RoleUI;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Alexey on 09.01.2015.
 */
@Remote
public interface DbRoles {

    public List<RoleUI> getAllRoles();
}
