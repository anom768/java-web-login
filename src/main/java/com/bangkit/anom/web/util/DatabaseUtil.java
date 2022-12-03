package com.bangkit.anom.web.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseUtil {

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(2);
        config.setMinimumIdle(1);
        config.setIdleTimeout(36_000);
        config.setMaxLifetime(36_000);

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/java_web_login");

        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

}
