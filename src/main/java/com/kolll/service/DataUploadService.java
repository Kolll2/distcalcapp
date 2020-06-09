package com.kolll.service;

import com.kolll.model.database.Database;
import com.kolll.model.entities.XMLCities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.SQLException;

public class DataUploadService {

    public static boolean upload(File tempFile) {

        try {
            JAXBContext jc = JAXBContext.newInstance(XMLCities.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLCities cities = (XMLCities) unmarshaller.unmarshal(tempFile);

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
}
