package ru.tsu.dnevnik.db.models;

import java.io.Serializable;

/**
 * Created by Alexey on 01.10.2016.
 */
public class StudentUI implements Serializable {
    public Integer id;
    public String name;

    @Override
    public String toString() {
        return "StudentUI{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
