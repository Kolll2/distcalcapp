package com.kolll.web;

import com.kolll.model.database.Database;
import com.kolll.model.entities.City;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Path("/cities")
public class Cities {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer, String> printCities() {
        Map<Integer, String> result = null;
        try {
            result = Database.getInstance().getCities();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Path("insert")
    public Response addCity() {
        List<City> temp = new ArrayList<>();
        temp.add(new City("Moscow", 55.7522f, 37.6156f));
        temp.add(new City("Samara", 53.2001f, 50.15f));
        temp.add(new City("Kazan", 55.798551f, 49.106324f));

        try {
            Database.getInstance().insertCities(temp);
            return Response.status(200).entity("Cities added").build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(500).entity("Something went wrong").build();
    }
}
