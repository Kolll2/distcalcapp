package com.kolll.service;

import com.kolll.model.database.Database;
import com.kolll.model.exception.NoCityInDatabaseException;

import java.sql.SQLException;
import java.util.Map;

public class DataAccessService {

    public static Map<Integer, String> getCities() throws SQLException {
        return Database.getInstance().getCities();
    }

    public static Integer getDistance(String from, String to) throws NoCityInDatabaseException, SQLException {
        return Database.getInstance().getDistance(from, to);
    }

}
