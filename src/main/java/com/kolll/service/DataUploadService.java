package com.kolll.service;

import com.kolll.model.database.Database;
import com.kolll.model.entities.City;
import com.kolll.model.entities.Distance;
import com.kolll.model.entities.XMLCities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataUploadService {

    public static boolean upload(File tempFile) {
        try {
            XMLCities cities = unmarshalCities(tempFile);

            Database database = Database.getInstance();

            database.clearDB();
            database.insertCities(cities.getCities());
            database.insertDistances(cities.getDistances());

        } catch (JAXBException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean update(File tempFile) {
        try {

            XMLCities cities = unmarshalCities(tempFile);

            Database database = Database.getInstance();

            database.updateCities(cities.getCities());
            database.updateDistances(cities.getDistances());

        } catch (JAXBException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean unification(File tempFile) {
        try {
            XMLCities cities = unmarshalCities(tempFile);

            City[] citiesArray = new City[cities.getCities().size()];
            citiesArray = cities.getCities().toArray(citiesArray);
            mergerCities(citiesArray);

            Distance[] distancesArray = new Distance[cities.getDistances().size()];
            distancesArray = cities.getDistances().toArray(distancesArray);
            mergingDistances(distancesArray);

        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }

    private static XMLCities unmarshalCities(File tempFile) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(XMLCities.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (XMLCities) unmarshaller.unmarshal(tempFile);
    }

    public static void mergerCities(City... cities) {
        List<City> insertList = new ArrayList<>();
        List<City> updateList = new ArrayList<>();
        for (City city : cities) {
            if (Database.getInstance().checkExistenceCity(city.getName())) {
                updateList.add(city);
            } else {
                insertList.add(city);
            }
        }

        try {
            Database.getInstance().insertCities(insertList);
            Database.getInstance().updateCities(updateList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void mergingDistances(Distance ... distances) {
        List<Distance> insertList = new ArrayList<>();
        List<Distance> updateList = new ArrayList<>();

        for (Distance distance : distances) {
            if (Database.getInstance().checkExistenceDistance(distance.getFromCity(), distance.getToCity())) {
                updateList.add(distance);
            } else {
                insertList.add(distance);
            }
        }

        try {
            Database.getInstance().insertDistances(insertList);
            Database.getInstance().updateDistances(updateList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
