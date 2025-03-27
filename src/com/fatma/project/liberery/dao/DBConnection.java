/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fatma.project.liberery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author A M
 */
public class DBConnection {

    private final static String URL = "jdbc:mysql://localhost:3306/liberery_system_java";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "123456";

    public static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        return connection;
    }
}
