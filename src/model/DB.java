package model;

import java.sql.*;
public class DB {
    static String db = "jdbc:sqlite:final.sqlite";
    static Connection conn;

    public static void init() {
        try {
            conn = DriverManager.getConnection(db);
            Statement s = conn.createStatement();
            s.executeUpdate("PRAGMA foreign_keys = ON; ");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    static private int handleStatement(String SQL) {
        try {
            System.out.println(SQL);
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.execute();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private static ResultSet handleSelect(String SQL) {
        try {
            PreparedStatement stmt = conn.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}