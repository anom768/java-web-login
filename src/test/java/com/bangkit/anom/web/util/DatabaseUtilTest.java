package com.bangkit.anom.web.util;

import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseUtilTest {

    @Test
    void testConnection() throws SQLException {
        var dataSource = DatabaseUtil.getDataSource();
        var connection = dataSource.getConnection();

        Assertions.assertNotNull(connection);

        dataSource.close();
        connection.close();
    }

}
