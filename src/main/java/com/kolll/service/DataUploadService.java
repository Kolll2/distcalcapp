package com.kolll.service;

import com.kolll.model.database.Database;
import com.kolll.model.entities.City;
import com.kolll.model.entities.Distance;
import com.kolll.model.entities.XMLCities;
import com.kolll.web.Cities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataUploadService {

    public static boolean upload(File tempFile) {

        try {
            JAXBContext jc = JAXBContext.newInstance(XMLCities.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLCities cities = (XMLCities) unmarshaller.unmarshal(tempFile);

            Database database = Database.getInstance();
            database.insertCities(cities.getCities());
            database.insertDistances(cities.getDistances());

        } catch (JAXBException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean save() {

        try {

            List<City> cities = new ArrayList<>();
            XMLCities xmlCities = new XMLCities();


            cities.add(new City("Samara", 55.555f, 44.4444f));
            cities.add(new City("Moscow", 22.555f, 11.4444f));
            cities.add(new City("Samara", 33.555f, 99.4444f));

            xmlCities.setCities(cities);

            List<Distance> distances = new ArrayList<>();
            distances.add(new Distance(1L,2L,500));
            distances.add(new Distance(2L,1L,500));

            xmlCities.setDistances(distances);

            JAXBContext jc = JAXBContext.newInstance(XMLCities.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter xmlWriter = new StringWriter();
            marshaller.marshal(xmlCities, xmlWriter);
            System.out.println(xmlWriter.toString());

        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean savedist() {

        try {

            List<City> cities = new ArrayList<>();

            cities.add(new City("Samara", 55.555f, 44.4444f));
            cities.add(new City("Moscow", 22.555f, 11.4444f));
            cities.add(new City("Samara", 33.555f, 99.4444f));

            JAXBContext jc = JAXBContext.newInstance(Distance.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter xmlWriter = new StringWriter();
            marshaller.marshal(new Distance(1L,2L,500), xmlWriter);
            System.out.println(xmlWriter.toString());

        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
