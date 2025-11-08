package com.sam.sidTask.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {

    private static final String PROPS = "/application.properties";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    static {
        try (InputStream in = DbUtil.class.getResourceAsStream(PROPS)) {
            Properties props = new Properties();
            props.load(in);
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
            // Register driver not necessary with modern drivers but safe:
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Failed to load DB config: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
