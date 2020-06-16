package com.kolll.service;

import com.kolll.model.database.Database;
import com.kolll.model.exception.NoCityInDatabaseException;

import java.util.List;

public class CalculationService {
    private static final int EARTH_RADIUS = 6_372_795; // meters

    public static Integer getCrow(String from, String to) throws NoCityInDatabaseException {

        List<Float> city1 = Database.getInstance().getPos(from);
        List<Float> city2 = Database.getInstance().getPos(to);

        double latDelta = Math.toRadians(city2.get(0) - city1.get(0));
        double lonDelta = Math.toRadians(city2.get(1) - city1.get(1));
        double a = Math.sin(latDelta / 2) * Math.sin(latDelta / 2)
                + Math.cos(Math.toRadians(city1.get(0))) * Math.cos(Math.toRadians(city2.get(0)))
                * Math.sin(lonDelta / 2) * Math.sin(lonDelta / 2);
        return (int) Math.round((EARTH_RADIUS / 1000) * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}
