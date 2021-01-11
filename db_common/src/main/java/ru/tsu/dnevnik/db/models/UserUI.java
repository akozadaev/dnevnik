package ru.tsu.dnevnik.db.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

/**
 * Created by Alexey on 04.01.2015.
 */
public class UserUI implements Serializable {


    public Integer id;
    public String login;
    public String email;
    public String last_name;
    public String first_name;
    public String middle_name;
    public String about;
    public String phoneNumber;
    public Integer password_hash;
    public Integer role;
    public Integer region;
    public Time create_time;
    public Time last_time;
    public Time stop_time;
    public Time expired_time;
    public Integer try_count;
    public String accountRede;
    public String roleRede;
    public List<Integer> task_ids;

    @Override
    public String toString() {
        return "UserUI{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", about='" + about + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password_hash=" + password_hash +
                ", role=" + role +
                ", region=" + region +
                ", create_time=" + create_time +
                ", last_time=" + last_time +
                ", stop_time=" + stop_time +
                ", expired_time=" + expired_time +
                ", try_count=" + try_count +
                ", accountRede='" + accountRede + '\'' +
                ", roleRede='" + roleRede + '\'' +
                ", task_ids=" + task_ids +
                '}';
    }
}
