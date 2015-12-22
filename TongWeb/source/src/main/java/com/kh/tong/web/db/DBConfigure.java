package com.kh.tong.web.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.kh.db.ConnectionProvider;

public class DBConfigure implements ConnectionProvider {
    static Logger logger = Logger.getLogger(DBConfigure.class);

    public static final String MySQLDriver = "com.mysql.jdbc.Driver";
    private static final String DevJDBCString = "jdbc:mysql://localhost:3306/tong?useUnicode=true&autoReconnect=true&characterEncoding=UTF-8";
    private static final String DevUserName = "tong";
    private static final String DevPassword = "tong";

    private static DBConfigure instance = new DBConfigure();

    public static boolean initWithJDBC(String driverName, String connString, String user, String pass) {
        if (instance.initComplete)
            return true;
        if (!loadJDBCDriver(driverName)) {
            return false;
        }
        instance.driverClassName = driverName;
        instance.jdbcConnString = connString;
        instance.jdbcUserName = user;
        instance.jdbcPassword = pass;
        instance.initComplete = true;
        return true;
    }

    private static boolean loadJDBCDriver(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("Error initializing JDBC driver : " + className, e);
            return false;
        }
        return true;
    }

    private DataSource dataSource;
    private boolean initComplete = false;
    @SuppressWarnings("unused")
    private String driverClassName = null;
    private String jdbcConnString = null;
    private String jdbcUserName = null;
    private String jdbcPassword = null;

    protected DBConfigure() {
        dataSource = null;
    }

    public static DBConfigure getConfigure() {
        if (!instance.initComplete) {
            initWithJDBC(MySQLDriver, DevJDBCString, DevUserName, DevPassword);
        }
        return instance;
    }

    public Connection getDbConnection() throws SQLException {
        if (!initComplete)
            return null;
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        if (jdbcConnString != null) {
            Connection conn = null;
            if (jdbcUserName != null) {
                conn = DriverManager.getConnection(jdbcConnString, jdbcUserName, jdbcPassword);
            } else {
                conn = DriverManager.getConnection(jdbcConnString);
            }
            return conn;
        }
        return null;
    }

}
