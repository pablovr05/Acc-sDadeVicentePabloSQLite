package com.project;

import java.sql.*;
import java.util.ArrayList;

public class UtilsSQLite {

    public static Connection connect(String filePath) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + filePath;
            conn = DriverManager.getConnection(url);
            System.out.println("Conectado a SQLite");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void disconnect(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int queryUpdate(Connection conn, String sql) {
        int result = 0;
        try (Statement stmt = conn.createStatement()) {
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ResultSet querySelect(Connection conn, String sql) {
        ResultSet rs = null;
        try (Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
