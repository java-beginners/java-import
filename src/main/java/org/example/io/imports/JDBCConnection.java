package org.example.io.imports;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConnection {
    private String DB_URL;
    private String USER_NAME;
    private String PASSWORD;

    public JDBCConnection(String DB_URL, String USER_NAME, String PASSWORD) {
        this.DB_URL = DB_URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }
    public Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    };
}
