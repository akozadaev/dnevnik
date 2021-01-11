package ru.tsu.dnevnik.db.beans;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.tsu.dnevnik.db.interfaces.DbRoles;
import ru.tsu.dnevnik.db.models.RoleUI;

import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

/**
 * Created by Alexey on 09.01.2015.
 */
@Singleton(mappedName = "DbRolesBean", name = "DbRolesBean")
@Startup
@TransactionAttribute(REQUIRES_NEW)
@ApplicationException(rollback = true)
public class DbRolesBean implements DbRoles{
    JdbcTemplate jdbcTemplate;
    static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DbRolesBean.class);
    private final static RolesUIMapper ROLE_UI_MAPPER = new RolesUIMapper();
    private final String SELECT_ROLES_ID_BY_REDE = "SELECT id\n" +
            "  FROM roles WHERE rede = ?";
    private final String SELECT_ALL_ROLES = "SELECT id, role, roles_rede\n" +
            "  FROM roles;\n";
    private final String SELECT_ALL_ROLES_REDE = "SELECT rede  FROM roles  ORDER BY id DESC LIMIT 2;";
    @Resource(mappedName = "jdbc/dnevnikDs")
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<RoleUI> getAllRoles() {
        try {
            LOGGER.debug("Get all roles");
            return jdbcTemplate.query(SELECT_ALL_ROLES, ROLE_UI_MAPPER);
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.debug("Don't get all roles, error ", ex);
            return null;
        }
    }

    private static class RolesUIMapper

            implements RowMapper<RoleUI> {

        @Override
        public RoleUI mapRow(ResultSet rs, int i) throws SQLException {
            RoleUI res = new RoleUI();
            res.id = rs.getInt("id");
            res.role = rs.getString("role");
            res.roles_rede = rs.getString("roles_rede");
            return res;
        }
    }
}
