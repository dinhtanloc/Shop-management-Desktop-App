package com.example.app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class MySQLConnector {
    String url = "jdbc:mysql://localhost:3307/myshop";

    private String username="root";

    private String password="0123456789";

    Connection connection;
    public static MySQLConnector instance = null;
    private MySQLConnector(){

    }
    public static MySQLConnector getInstance(){
        if (instance ==null){
            instance = new MySQLConnector();
        }
        return instance;
    }
    public boolean Connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connect successful.........");
            return true;
        } catch (Exception  ex) {
            System.out.println(ex);
            System.out.println("Connect fail..........");
        }
        return false;
    }
    public boolean queryUpdate(String sql){
        if (connection != null){
            try{
                connection.createStatement().executeUpdate(sql);
                return true;
            }catch (Exception ex){
                System.out.println(ex);
                return false;
            }

        }
        return false;
    }
    public ResultSet queryResults(String sql){
        if (connection != null){
            try{
                return connection.createStatement().executeQuery(sql);
            }catch (Exception ex){
                System.out.println(ex);
            }

        }
        return null;
    }
}
