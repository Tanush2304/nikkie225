package com.nichi.nikkie225.model;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static final Dotenv dotenv=Dotenv.load();
    public static final String URL=dotenv.get("DATABASE_URL");
    public static final String USERNAME=dotenv.get("DATABASE_NAME");
    public static final String PASSWORD=dotenv.get("DATABASE_PASSWORD");


    public static Connection getConnection() throws SQLException{

        return  DriverManager.getConnection(URL,USERNAME,PASSWORD);


    }
}
