package ru.tsu.dnevnik.db.beans;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.tsu.dnevnik.db.interfaces.DbUsers;
import ru.tsu.dnevnik.db.models.UserUI;

import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.sql.DataSource;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

/**
 * Created by Alexey on 04.01.2015.
 */
@Singleton(mappedName = "DbUsersBean", name = "DbUsersBean")
@Startup
@TransactionAttribute(REQUIRES_NEW)
@ApplicationException(rollback = true)
public class DbUsersBean implements DbUsers {
    private static final String SELECT_USER_BY_ID = "SELECT id, login, email, displayname, phonenumber, password_hash, role, region, about, \n" +
            "       create_time, last_time, stop_time, expired_time, try_count, task_ids\n" +
            "  FROM accounts WHERE id =?;";
    private static final String SELECT_PRINTER_BY_ORGANIZATION_NAME = "SELECT  printer_infos.id, printer_infos.account_id, printer_infos.organizationname, printer_infos.siteadress, printer_infos.services, printer_infos.tarifs, printer_infos.options_getting, printer_infos.about, printer_infos.rating,\n" +
            "accounts.email, accounts.phonenumber, printer_type.type, regions.name\n" +
            "    FROM printer_infos\n" +
            "    INNER JOIN accounts ON (accounts.id = printer_infos.account_id)\n" +
            "    INNER JOIN printer_type ON (printer_infos.printer_type = printer_type.id)\n" +
            "    INNER JOIN regions ON (accounts.region = regions.id)\n" +
            "    \n" +
            "    WHERE printer_infos.organizationname =  ?;",

    SELECT_PRINTER_BY_ORGANIZATION_PRINTER_ID = "SELECT  printer_infos.id, printer_infos.account_id, printer_infos.organizationname, printer_infos.siteadress, printer_infos.services, printer_infos.tarifs, printer_infos.options_getting, printer_infos.about, printer_infos.rating,\n" +
            "\taccounts.email, accounts.phonenumber, printer_type.type, regions.name\n" +
            "        FROM printer_infos\n" +
            "        INNER JOIN accounts ON (accounts.id = printer_infos.account_id)\n" +
            "        INNER JOIN printer_type ON (printer_infos.printer_type = printer_type.id)\n" +
            "    INNER JOIN regions ON (accounts.region = regions.id)\n" +
            "        WHERE accounts.id = ?;";
    private static final String SELECT_PRINTER_BY_ID = "SELECT printer_type, organizationname, siteadress, services,\n" +
            "                tarifs, options_getting, about, logo, rating\n" +
            "        FROM printer_infos WHERE account_id = ?;",
            GET_ALL_PRINTERS_INFOS = "SELECT  printer_infos.id, printer_infos.account_id, printer_infos.organizationname, printer_infos.siteadress, printer_infos.services, printer_infos.tarifs, printer_infos.options_getting, printer_infos.about, printer_infos.rating,\n" +
                    "\n" +
                    "accounts.email, accounts.phonenumber, printer_type.type, regions.name\n" +
                    "    FROM printer_infos\n" +
                    "    INNER JOIN accounts ON (accounts.id = printer_infos.account_id)\n" +
                    "    INNER JOIN printer_type ON (printer_infos.printer_type = printer_type.id)\n" +
                    "    INNER JOIN regions ON (accounts.region = regions.id)\n" +
                    " ORDER BY rating DESC;";
    private static final String SELECT_PRINTER_BY_ID_AND_REGION = "SELECT printer_type, organizationname, siteadress, services,\n" +
            "                tarifs, options_getting, about, logo, rating\n" +
            "        FROM printer_infos WHERE account_id = ?;";
    private static final String SELECT_USER_BY_LOGIN = "SELECT id, login, email, phoneNumber, last_name, first_name, middle_name, password_hash, role, region,  about,\n" +
            "       create_time, last_time, stop_time, expired_time, try_count, task_ids\n" +
            "  FROM accounts WHERE login =?;";
    private static final String SELECT_USERS_BY_ROLE = "SELECT id, email, login, displayname, phonenumber, password_hash, region, role, about, last_time, stop_time, create_time, expired_time, try_count, task_ids\n" +
            "  FROM accounts WHERE role = ?;";
    private static final String SELECT_USERS_BY_ROLE_AND_REGIONID = "SELECT id, email, login, displayname, phonenumber, password_hash, region, role, about, last_time, stop_time, create_time, expired_time, try_count, task_ids\n" +
            "              FROM accounts WHERE role = ? AND region = ?;";
    private final static String CREATE_USER = "INSERT INTO accounts(login, email, last_name, first_name, middle_name, password_hash, role, \n" +
            "            create_time, try_count, status)\n" +
            "    VALUES (?,?,?,?,?,?,?,\n" +
            "            LOCALTIMESTAMP, 0, ?);";
    private final static String UPDATE_USER_NAME = "UPDATE accounts\n" +
            "   SET displayname=?\n" +
            "       \n" +
            " WHERE id=?;";
    private final static String UPDATE_USER_ADDRESS = "UPDATE accounts\n" +
            "   SET about=?\n" +
            "       \n" +
            " WHERE id=?;",
            UPDATE_USER_PHONENUMBER = "UPDATE accounts\n" +
            "   SET phonenumber=?\n" +
            "       \n" +
            " WHERE id=?;",
            CHANCHE_LOGIN_AND_PASS_BY_ID="UPDATE accounts\n" +
                    "   SET login=?, email=?, password_hash=?\n" +
                    " WHERE id = ?;",
    GET_PRINTERUI_LIST_BY_REGION_ID =  "SELECT  printer_infos.id, printer_infos.account_id, printer_infos.organizationname, printer_infos.siteadress, printer_infos.services, printer_infos.tarifs, printer_infos.options_getting, printer_infos.about, printer_infos.rating,\n" +
            "\taccounts.email, accounts.phonenumber, printer_type.type, regions.name\n" +
            "        FROM printer_infos\n" +
            "        INNER JOIN accounts ON (accounts.id = printer_infos.account_id)\n" +
            "        INNER JOIN printer_type ON (printer_infos.printer_type = printer_type.id)\n" +
            "        INNER JOIN regions ON (accounts.region = regions.id)\n" +
            "        WHERE accounts.region = ? ORDER BY printer_infos.rating DESC;\n" +
            "                ";

    JdbcTemplate jdbcTemplate;
    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DbUsersBean.class);
    private final static UserUIMapper USER_UI_MAPPER = new UserUIMapper();
    private final static UserUISmallMapper USER_UI_SMALL_MAPPER = new UserUISmallMapper();

    @Resource(mappedName = "jdbc/dnevnikDs")
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public UserUI getUserUI(String login) {
        try {
            logger.debug("getUserUI");
            return jdbcTemplate.queryForObject(SELECT_USER_BY_LOGIN, USER_UI_MAPPER, login);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("getUserUI, error ", ex);
            return null;
        }
    }

    @Override
    public List<UserUI> getAllUsers() {
        try {
            logger.debug("getUserUI");
            return jdbcTemplate.query("SELECT accounts.id, accounts.last_name, accounts.first_name, accounts.middle_name, \n" +
                    "       account_status.account_status_rede, roles.roles_rede\n" +
                    "  FROM accounts\n" +
                    "  INNER JOIN account_status ON (accounts.status = account_status.id)\n" +
                    "  INNER JOIN roles ON (accounts.role = roles.id)\n", USER_UI_SMALL_MAPPER);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("getUserUI, error ", ex);
            return null;
        }
    }

    @Override
    public int createUser(String login, String last_name, String first_name, String middle_name, int password_hash, int role, int status) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        jdbcTemplate.update(CREATE_USER, login, login, last_name, first_name, middle_name,password_hash, role, status);
        System.out.println("????????????????????????????????");
        return jdbcTemplate.queryForObject(SELECT_USER_BY_LOGIN, USER_UI_MAPPER, login).id;
    }


    private static class UserUISmallMapper
            implements RowMapper<UserUI> {

        @Override
        public UserUI mapRow(ResultSet rs, int i) throws SQLException {
            UserUI res = new UserUI();
            res.id = rs.getInt("id");
            res.last_name = rs.getString("last_name");
            res.first_name = rs.getString("first_name");
            res.middle_name = rs.getString("middle_name");
            res.accountRede = rs.getString("account_status_rede");
            res.roleRede = rs.getString("roles_rede");

            System.out.println("res.toString() " + res.toString());
            return res;
        }
    }



    private static class UserUIMapper
            implements RowMapper<UserUI> {

        @Override
        public UserUI mapRow(ResultSet rs, int i) throws SQLException {
            UserUI res = new UserUI();
            res.id = rs.getInt("id");
            res.login = rs.getString("login");
            res.email = rs.getString("email");
            res.last_name = rs.getString("last_name");
            res.first_name = rs.getString("first_name");
            res.middle_name = rs.getString("middle_name");
            res.phoneNumber = rs.getString("phonenumber");
            res.password_hash = rs.getInt("password_hash");
            res.role = rs.getInt("role");
            res.region = rs.getInt("region");
            res.about = rs.getString("about");
            res.create_time = rs.getTime("create_time");
            res.last_time = rs.getTime("last_time");
            res.stop_time = rs.getTime("stop_time");
            res.expired_time = rs.getTime("expired_time");
            res.try_count = rs.getInt("try_count");
            Array array = rs.getArray("task_ids");
            if (array != null) {
                res.task_ids = new ArrayList<Integer>(Arrays.asList((Integer[]) array.getArray()));
                System.out.println("\n++++++++++++++++++++++++++++\n" + res.task_ids);
            }
            System.out.println("res.toString() " + res.toString());
            return res;
        }
    }


}
