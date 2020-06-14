package com.kolll.web;

import com.kolll.model.exception.NoCityInDatabaseException;
import com.kolll.service.CalculationService;
import com.kolll.service.DataAccessService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.*;

@Path("/distance")
public class Distances {

    @GET
    @Path("{type}/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType(@PathParam("type") String type, @PathParam("from") String from, @PathParam("to") String to) {
        return getResponseAll(type, from, to);
    }

    @POST
    @Path("{type}/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postType(@PathParam("type") String type, @PathParam("from") String from, @PathParam("to") String to) {
        return getResponseAll(type, from, to);
    }

    private Response getResponseAll(String type, String from, String to) {
        Map<String, Integer> result = new HashMap<>();

        try {
            switch (type) {
                case ("crow"):
                    result.put("Crowflight", CalculationService.getCrow(from, to));
                    break;
                case ("matrix"):
                    result.put("Distance Matrix", DataAccessService.getDistance(from, to));
                    break;
                case ("all"): {
                    result.put("Crowflight", CalculationService.getCrow(from, to));
                    result.put("Distance Matrix", DataAccessService.getDistance(from, to));
                }
                break;
                default:
                    throw new NoCityInDatabaseException();
            }
        } catch (NoCityInDatabaseException e) {
            return Response.status(400).entity("coordinates of these cities are not available").build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(200).entity(result).build();
    }
}
