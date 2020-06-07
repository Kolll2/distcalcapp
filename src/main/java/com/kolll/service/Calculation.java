package com.kolll.service;

import com.kolll.model.database.Database;
import com.kolll.model.entities.City;

import java.util.List;

public class Calculation {
    public static Integer getCrow(String from, String to) {
//        Математические константы, радиус земли и число pi (Math.PI)
        int radius = 6372795;

        List<Float> city1 = Database.getInstance().getPos(from);
        List<Float> city2 = Database.getInstance().getPos(to);

// #в радианах
        Double lat1 = (city1.get(0) * Math.PI) / 180;
        Double long1 = (city1.get(1) * Math.PI) / 180;
        Double lat2 = (city2.get(0) * Math.PI) / 180;
        Double long2 = (city2.get(1) * Math.PI) / 180;

// #косинусы и синусы широт и разницы долгот
        Double cl1 = Math.acos(lat1);
        Double cl2 = Math.acos(lat2);
        Double sl1 = Math.asin(lat1);
        Double sl2 = Math.asin(lat2);
        Double delta = long2 - long1;
        Double cdelta = Math.acos(delta);
        Double sdelta = Math.asin(delta);

// #вычисления длины большого круга
        Double y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
        Double x = sl1 * sl2 + cl1 * cl2 * cdelta;

        Double ad = Math.atan2(y, x);
        Double dist = ad * radius;

        return dist.intValue();
    }
}
