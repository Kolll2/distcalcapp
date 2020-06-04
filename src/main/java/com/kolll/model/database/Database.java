package com.kolll.model.database;

import com.kolll.model.entities.City;
import com.kolll.model.entities.Distance;

import java.sql.*;
import java.util.List;

public class Database {

    // Singleton
    private static Database instance;

    private static final String url = "jdbc:mysql://localhost:3306/distance-calculator?autoReconnect=true&useSSL=false";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    private Database() {
        open();
    }

    public static Database getInstance() {
        if(instance == null) instance = new Database();
        return instance;
    }

    public void open(){
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    public void close(){
        try {
            connection.close();
        } catch (SQLException ex) { }
        try {
            statement.close();
        } catch (SQLException ex) {  }
        try {
            resultSet.close();
        } catch (SQLException ex) { }
    }

    /**
     * queries to the database
     */

    public List<String> getCities() throws SQLException {
        return null;
    }

    public String getDistance(String fromCity, String toCity) throws SQLException {
        return null;
    }

    public String[] getPos(String city) throws SQLException {
        return null;
    }

    private int getCityIdByName(String city) throws SQLException {
        return 0;
    }

    public void insertCities(List<City> cities) throws SQLException {

    }

    public void insertDistances(List<Distance> distances) throws SQLException {

    }
}
