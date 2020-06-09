package com.kolll.service;

import com.kolll.model.database.Database;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class DataAccessService {

    public static Integer getDistance(String from, String to) {
        Integer result = null;
        try {
            result = Database.getInstance().getDistance(from, to);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<Integer, String> getCities() {
        Map<Integer, String> result = new TreeMap<>();
        try {
            result = Database.getInstance().getCities();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
