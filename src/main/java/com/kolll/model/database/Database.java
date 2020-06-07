package com.kolll.model.database;

import com.kolll.model.entities.City;
import com.kolll.model.entities.Distance;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {

    //    private static final String url = "jdbc:mysql://localhost:3306/distance-calculator?autoReconnect=true&useSSL=false";
    private static final String url = "jdbc:mysql://localhost:3306/distance-calculator?serverTimezone=Europe/Moscow";
    private static final String user = "root";
    private static final String password = "root";
    // Singleton
    private static Database instance;
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    private Database() {
        open();
    }

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    public void open() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
        }
        try {
            statement.close();
        } catch (SQLException ex) {
        }
        try {
            resultSet.close();
        } catch (SQLException ex) {
        }
    }

    /**
     * queries to the database
     */

    public List<String> getCities() throws SQLException {

        String part1 = "SELECT city_id, name FROM cities";
        List<String> result = new ArrayList<String>();
        resultSet = statement.executeQuery(part1);
        while (resultSet.next()) {
            result.add(resultSet.getInt(1) + ":" + resultSet.getString(2));
        }

        return result;
    }

    public List<String> getDistances() throws SQLException {
        String part1 = "SELECT distance_id, from_city.name AS fromCity, cities.name AS toCity, distance FROM distances" +
                " LEFT JOIN cities AS from_city ON distances.from_city=city_id" +
                " LEFT JOIN cities ON distances.to_city = cities.city_id";
        List<String> result = new ArrayList<>();
        resultSet = statement.executeQuery(part1);
        while (resultSet.next()) {
            result.add(resultSet.getInt(1) + ":" + resultSet.getString(2) +
                    " => " + resultSet.getString(3) + " ... " + resultSet.getInt(4));
        }
        return result;
    }


    public String getDistance(String fromCity, String toCity) throws SQLException {
        Integer result = 0;
        String part1 = "SELECT distance FROM distances" +
                " LEFT JOIN cities AS from_city ON distances.from_city=city_id" +
                " LEFT JOIN cities ON distances.to_city = cities.city_id" +
                " WHERE from_city.name=\'" + fromCity + "\' AND cities.name=\'" + toCity + "\'";
        resultSet = statement.executeQuery(part1);
        if (resultSet.next())
            result = resultSet.getInt(1);

        return result.toString();
    }

    public List<Float> getPos(String city) {
        List<Float> result = new ArrayList<>();
        String part1 = "SELECT latitude, latitude FROM cities" +
                " WHERE cities.name=\'" + city + "\'";
        try {
            resultSet = statement.executeQuery(part1);
            if (resultSet.next()) {
                result.add(resultSet.getFloat(1));
                result.add(resultSet.getFloat(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private int getCityIdByName(String city) throws SQLException {
        int result = 0;
        String part1 = "SELECT city_id FROM cities" +
                " WHERE cities.name=\'" + city + "\'";
        resultSet = statement.executeQuery(part1);
        if (resultSet.next())
            result = resultSet.getInt(1);
        return result;
    }

    public void insertCities(List<City> cities) throws SQLException {
        System.out.println("=========================================");
        System.out.println("insertCities(List<City> cities)");
        System.out.println(Arrays.toString(cities.toArray()));

//        String part1 = "INSERT INTO cities (name, latitude, longitude) VALUES ";
        StringBuffer query = new StringBuffer("INSERT INTO cities (name, latitude, longitude) VALUES ");
        String part2 = "(\"%s\", \"%s\", \"%s\"),";

        for (City city : cities) {
            query.append(String.format(part2, city.getName(), city.getLatitude(), city.getLongitude()));
        }
        query.replace(query.lastIndexOf(","), query.length(), ";");
        statement.execute(query.toString());
    }

    public void insertDistances(List<Distance> distances) throws SQLException {
        System.out.println("=========================================");
        System.out.println("insertDistances(List<Distance> distances)");
        System.out.println(Arrays.toString(distances.toArray()));

        StringBuffer query = new StringBuffer("INSERT INTO distances (from_city, to_city, distance) VALUES ");
        String part2 = "(\"%s\", \"%s\", \"%s\"),";

        for (Distance distance : distances) {
            query.append(String.format(part2, distance.getFromCity(), distance.getToCity(), distance.getDistance()));
        }
        query.replace(query.lastIndexOf(","), query.length(), ";");
        statement.execute(query.toString());
    }
}
