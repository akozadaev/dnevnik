package ru.tsu.dnevnik.db.beans;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.tsu.dnevnik.db.interfaces.DbStudents;
import ru.tsu.dnevnik.db.models.StudentUI;

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

@Singleton(mappedName = "DbStudentsBean", name = "DbStudentsBean")
@Startup
@TransactionAttribute(REQUIRES_NEW)
@ApplicationException(rollback = true)
public class DbStudentsBean implements DbStudents {
    private static final String SELECT_STUDENT_BY_ID = "SELECT id, name " +
            "  FROM students WHERE id =?;";

    JdbcTemplate jdbcTemplate;
    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DbStudentsBean.class);
    private final static StudentUIMapper STUDENT_UI_MAPPER = new StudentUIMapper();

    @Resource(mappedName = "jdbc/dnevnikDs")
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public StudentUI getStudentUI(String login) {
        try {
            logger.debug("getStudentUI");
            return jdbcTemplate.queryForObject(SELECT_STUDENT_BY_ID, STUDENT_UI_MAPPER, login);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("getStudentUI, error ", ex);
            return null;
        }
    }

    @Override
    public List<StudentUI> getAllStudents() {
        try {
            logger.debug("getAllStudents");
            return jdbcTemplate.query("SELECT students.id, students.name " +
                    "  FROM students", STUDENT_UI_MAPPER);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("getAllStudents, error ", ex);
            return null;
        }
    }

    @Override
    public int createStudent(String name) {
        return jdbcTemplate.queryForInt("INSERT INTO students(name) VALUES (?) RETURNING id;", name);
    }

    private static class StudentUIMapper
            implements RowMapper<StudentUI> {

        @Override
        public StudentUI mapRow(ResultSet rs, int i) throws SQLException {
            StudentUI res = new StudentUI();
            res.id = rs.getInt("id");
            res.name = rs.getString("name");

            System.out.println("res.toString() " + res.toString());
            return res;
        }
    }
}
