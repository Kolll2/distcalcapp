package com.kolll.model.database;

import com.kolll.model.entities.City;
import com.kolll.model.entities.Distance;
import com.kolll.model.exception.NoCityInDatabaseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.*;
import java.sql.*;
import java.util.*;

public class Database {

    private static final String url = "jdbc:mysql://localhost:3306/distance-calculator?serverTimezone=Europe/Moscow";
    private static final String user = "root";
    private static final String password = "root";
    // Singleton
    private static Database instance;
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    private static DataSource ds = null;
    private static Context ctx = null;

    private Database() {
        open();
    }

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    public void open() {
        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/magenta/datasource/test-distance-calculator");
//            connection = DriverManager.getConnection(url, user, password);
            connection = ds.getConnection();
            System.out.println("$$$$  " + connection);
            statement = connection.createStatement();
            System.out.println("$$$$  " + statement);
        } catch (SQLException | NamingException ex) {
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

    public Map<Integer, String> getCities() throws SQLException {

        String part1 = "SELECT city_id, name FROM cities";
        Map<Integer, String> result = new TreeMap<>();
        resultSet = statement.executeQuery(part1);
        while (resultSet.next()) {
            result.put(resultSet.getInt(1), resultSet.getString(2));
        }

        return result;
    }

    public List<String> getDistances() throws SQLException, NoCityInDatabaseException {
        String part1 = "SELECT distance_id, from_city.name AS fromCity, cities.name AS toCity, distance FROM distances" +
                " LEFT JOIN cities AS from_city ON distances.from_city=city_id" +
                " LEFT JOIN cities ON distances.to_city = cities.city_id";
        List<String> result = new ArrayList<>();
        resultSet = statement.executeQuery(part1);

        if (!resultSet.next()) throw new NoCityInDatabaseException();

        while (resultSet.next()) {
            result.add(resultSet.getInt(1) + ":" + resultSet.getString(2) +
                    " => " + resultSet.getString(3) + " ... " + resultSet.getInt(4));
        }
        return result;
    }

    public Integer getDistance(Long fromCity, Long toCity) throws SQLException, NoCityInDatabaseException {
        Integer result = 0;
        String part1 = "SELECT distance FROM distances" +
                " WHERE from_city=\'" + fromCity + "\' AND to_city=\'" + toCity + "\'";
        resultSet = statement.executeQuery(part1);
        if (resultSet.next())
            result = resultSet.getInt(1);
        else throw new NoCityInDatabaseException();

        return result;
    }

    public Integer getDistance(String fromCity, String toCity) throws SQLException, NoCityInDatabaseException {
        Integer result = 0;
        String part1 = "SELECT distance FROM distances" +
                " LEFT JOIN cities AS from_city ON distances.from_city=city_id" +
                " LEFT JOIN cities ON distances.to_city = cities.city_id" +
                " WHERE from_city.name=\'" + fromCity + "\' AND cities.name=\'" + toCity + "\'";
        resultSet = statement.executeQuery(part1);
        if (resultSet.next())
            result = resultSet.getInt(1);
        else throw new NoCityInDatabaseException();

        return result;
    }

    public List<Float> getPos(String city) throws NoCityInDatabaseException {
        List<Float> result = new ArrayList<>();
        String part1 = "SELECT latitude, longitude FROM cities" +
                " WHERE cities.name=\'" + city + "\'";
        try {
            resultSet = statement.executeQuery(part1);
            if (resultSet.next()) {
                result.add(resultSet.getFloat(1));
                result.add(resultSet.getFloat(2));
            } else {
                throw new NoCityInDatabaseException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Long getCityIdByName(String city) throws SQLException, NoCityInDatabaseException {
        Long result = 0L;
        String part1 = "SELECT city_id FROM cities" +
                " WHERE cities.name=\'" + city + "\'";
        resultSet = statement.executeQuery(part1);
        if (resultSet.next())
            result = resultSet.getLong(1);
        else throw new NoCityInDatabaseException();
        return result;
    }

    public void insertCities(List<City> cities) throws SQLException {

        if (cities.size() < 1) return;

        StringBuffer query = new StringBuffer("INSERT INTO cities (name, latitude, longitude) VALUES ");
        String part2 = "(\"%s\", \"%s\", \"%s\"),";

        for (City city : cities) {
            query.append(String.format(part2, city.getName(), city.getLatitude(), city.getLongitude()));
        }

        query.replace(query.lastIndexOf(","), query.length(), ";");
        statement.execute(query.toString());
    }

    public void insertDistances(List<Distance> distances) throws SQLException {

        StringBuffer query = new StringBuffer("INSERT INTO distances (from_city, to_city, distance) VALUES ");
        String part2 = "(\"%s\", \"%s\", \"%s\"),";

        for (Distance distance : distances) {
            query.append(String.format(part2, distance.getFromCity(), distance.getToCity(), distance.getDistance()));
        }

        query.replace(query.lastIndexOf(","), query.length(), ";");
        statement.execute(query.toString());
    }

    public void updateCities(List<City> cities) throws SQLException {

        if (cities.size() < 1) return;

        String query = "UPDATE cities SET latitude = %s, longitude = %s WHERE name = \"%s\";";

        for (City city : cities) {
            statement.addBatch(String.format(query, city.getLatitude().toString(), city.getLongitude().toString(), city.getName()));
        }

        statement.executeBatch();
    }

    public void updateDistances(List<Distance> distances) throws SQLException {
        String query = "UPDATE distances SET distance = %d WHERE from_city = \"%s\" AND to_city = \"%s\";";

        for (Distance distance : distances) {
            statement.addBatch(String.format(query, distance.getDistance(), distance.getFromCity(), distance.getToCity()));
        }

        statement.executeBatch();
    }

    public void clearDB() {
        String query = "DELETE FROM distances;";
        try {
            statement.execute(query);
            query = "ALTER TABLE distances AUTO_INCREMENT=0;";
            statement.execute(query);
            query = "DELETE FROM cities";
            statement.execute(query);
            query = "ALTER TABLE cities AUTO_INCREMENT=0;";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean checkExistenceCity(String name) {
        String part1 = "SELECT city_id FROM cities" +
                " WHERE cities.name=\'" + name + "\'";
        try {
            resultSet = statement.executeQuery(part1);
            if (!resultSet.next())
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean checkExistenceDistance(Long from, Long to) {
        try {
            if (getDistance(from, to) > 0)
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoCityInDatabaseException e) {
            return false;
        }
        return true;
    }

    public boolean checkExistenceDistance(String from, String to) {
        try {
            if (getDistance(from, to) > 0)
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoCityInDatabaseException e) {
            return false;
        }
        return true;
    }

    public void insertDistanceByNamesCities(String from, String to, Integer distance) {
        try {
            getCityIdByName(from);
            getCityIdByName(to);
            String query = "INSERT INTO distances (from_city, to_city, distance) VALUES " +
                    "(\"" + from + "\", \"" + to + "\", \"" + distance + "\");";
            statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoCityInDatabaseException e) {
            e.printStackTrace();
        }


    }
}
