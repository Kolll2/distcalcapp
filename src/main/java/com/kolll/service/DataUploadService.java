package com.kolll.service;

import com.kolll.model.database.Database;
import com.kolll.model.entities.*;
import com.kolll.model.exception.IncorrectDataWasReceived;
import com.kolll.model.exception.NoCityInDatabaseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataUploadService {

    public static boolean unification(File tempFile) {
        try {
            XMLCities cities = unmarshalCities(tempFile);

            City[] citiesArray = new City[cities.getCities().size()];
            citiesArray = cities.getCities().toArray(citiesArray);
            mergerCities(citiesArray);

            try {
                checkDistanceForNull(cities);
            } catch (IncorrectDataWasReceived incorrectDataWasReceived) {
                cities = unmarshalCitiesFromString(tempFile);
            }

            Distance[] distancesArray = new Distance[cities.getDistances().size()];
            distancesArray = cities.getDistances().toArray(distancesArray);
            mergingDistances(distancesArray);

        } catch (JAXBException | IncorrectDataWasReceived e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /////////////////////////////////////////////
    //  JAXB unmarshal methods
    /////////////////////////////////////////////

    private static XMLCities unmarshalCities(File tempFile) throws JAXBException, IncorrectDataWasReceived {
        JAXBContext jc;
        XMLCities result;
        jc = JAXBContext.newInstance(XMLCities.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        result = (XMLCities) unmarshaller.unmarshal(tempFile);
        System.out.println("# unmarshalCities");
        System.out.println("## XMLCities: cities " + result.getCities().size() + ", distances " + result.getDistances().size());
        checkCityForNull(result);

        return result;
    }

    private static void checkCityForNull(XMLCities cities) throws IncorrectDataWasReceived {
        System.out.println("### checkCityForNull");
        for (City s : cities.getCities()){
            System.out.println(s);
            if (Objects.isNull(s.getName()) ||
                    Objects.isNull(s.getLatitude()) ||
                    Objects.isNull(s.getLongitude())){
                System.out.println(" find Null elements");
                throw new IncorrectDataWasReceived();
            }
        }
    }

    private static void checkDistanceForNull(XMLCities cities) throws IncorrectDataWasReceived {
        System.out.println("### checkDistanceForNull");
        for (Distance d : cities.getDistances()){
            System.out.println(d);
            if (Objects.isNull(d.getFromCity()) ||
                    Objects.isNull(d.getToCity()) ||
                    Objects.isNull(d.getDistance())){
                System.out.println(" find Null elements");
                throw new IncorrectDataWasReceived();
            }
        }
    }

    private static XMLCities unmarshalCitiesFromString(File tempFile) throws JAXBException {
        JAXBContext jc ;
        XMLCitiesString result ;
        jc = JAXBContext.newInstance(XMLCitiesString.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        result = (XMLCitiesString) unmarshaller.unmarshal(tempFile);
        System.out.println("# unmarshalCities");
        System.out.println("## XMLCities: cities " + result.getCities().size() + ", distances " + result.getDistances().size());
        return toXMLCities(result);
    }

    /////////////////////////////////////////////
    //  Combining the received data with the database
    /////////////////////////////////////////////

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
        System.out.println("##### mergingDistances");
        List<Distance> insertList = new ArrayList<>();
        List<Distance> updateList = new ArrayList<>();

        for (Distance distance : distances) {
            System.out.println(Database.getInstance().checkExistenceDistance(distance.getFromCity(), distance.getToCity()) +
                   " " + distance.toString());
            if (Database.getInstance().checkExistenceDistance(distance.getFromCity(), distance.getToCity())) {
                updateList.add(distance);
            } else {
                insertList.add(distance);
            }
        }

        System.out.println("Update :" + updateList.size());
        System.out.println("Insert :" + insertList.size());


        try {
            Database.getInstance().insertDistances(insertList);
            Database.getInstance().updateDistances(updateList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////
    //  Service
    /////////////////////////////////////////////

    private static XMLCities toXMLCities(XMLCitiesString xmlCitiesString) {
        XMLCities result = new XMLCities();
        result.setCities(xmlCitiesString.getCities());
        List<Distance> dist = new ArrayList<>();

        for (DistanceString ds : xmlCitiesString.getDistances()) {
            try {
                dist.add(new Distance(Database.getInstance().getCityIdByName(ds.getFromCity()),
                        Database.getInstance().getCityIdByName(ds.getToCity()),
                        ds.getDistance()));
                System.out.println(dist.toString());
            } catch (SQLException | NoCityInDatabaseException e) {
                e.printStackTrace();
            }
        }
        result.setDistances(dist);
        return result;
    }
}
