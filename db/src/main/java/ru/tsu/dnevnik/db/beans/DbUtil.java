package ru.tsu.dnevnik.db.beans;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbUtil {

    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DbUtil.class);

    /**
     * Позволяет выполнить sql-запрос с использованием {@link java.sql.Connection соединения}, контролируя жизненный цикл соединения.
     * @param dataSource источник данных, от которого запрашивается соединение
     * @param statement выполняемый запрос
     * @return <code>false</code> в случае если не удалось закрыть соединение
     * @throws java.sql.SQLException
     */
    public static boolean executeWithConnection(final DataSource dataSource, IExecWithConnectionStatement statement)
        throws SQLException, DataAccessException {

        boolean closeConnection = true;
        Connection connection = null;
        boolean noErrors = true;
        try {
            connection = dataSource.getConnection();
            if(dataSource instanceof SingleConnectionDataSource)
                closeConnection = false;

            statement.execute(connection);
        } finally {
            if(connection != null && closeConnection)
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.error("Failed to close connection", ex);
                    noErrors = false;
                }
        }

        return noErrors;
    }

    public static interface IExecWithConnectionStatement {
        void execute(Connection connection)
            throws SQLException, DataAccessException;
    }

}
