package org.alniss.notebook;

import java.sql.*;

public class DBManager {
    public DBManager() {
        initdb();
        inittable();
    }

    void initdb() {
        try (
                Connection conn = connect("");
                Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS tests;");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    void inittable() {
        try (
                Connection conn = connect("tests");
                Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test (id int, name varchar(255));");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    Connection connect(String dbname) {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbname + "?useSSL=false&allowPublicKeyRetrieval=true",
                    "user", "mysqlpassword");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    ResultSet query(String query) {
        try (
            Connection conn = connect("tests");
            Statement stmt = conn.createStatement();
        ) {
            System.out.println("SQL query: " + query);
            return stmt.executeQuery(query);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    int update(String update) {
        try (
            Connection conn = connect("tests");
            Statement stmt = conn.createStatement();
        ) {
            System.out.println("SQL update: " + update);
            return stmt.executeUpdate(update);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
