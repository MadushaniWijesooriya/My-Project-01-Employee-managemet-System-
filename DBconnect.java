/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeesystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnect {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/em", "root", ""  // DB URL, username, password
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}