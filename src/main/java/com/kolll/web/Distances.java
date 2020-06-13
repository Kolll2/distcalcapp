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
    @Path("get/{from}/{to}")
    public Response getDistance(@PathParam("from") String from, @PathParam("to") String to) {
        return getResponseDistance(from, to);
    }

    @POST
    @Path("get/{from}/{to}")
    public Response postDistance(@PathParam("from") String from, @PathParam("to") String to) {
        return getResponseDistance(from, to);
    }

    private Response getResponseDistance(String from, String to) {
        Integer result = -1;
        try {
            result = DataAccessService.getDistance(from, to);
            if (result == -1)
                throw new NoCityInDatabaseException();
        } catch (NoCityInDatabaseException e) {
            return Response.status(400).entity("this route is not in the database").build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(200).entity(result.toString()).build();
    }

    @GET
    @Path("crow/{from}/{to}")
    public Response getCrow(@PathParam("from") String from, @PathParam("to") String to) {
        try {
            return getResponseCrow(from, to);
        } catch (NoCityInDatabaseException e) {
            return Response.status(400).entity("coordinates of these cities are not available").build();
        }
    }

    @POST
    @Path("crow/{from}/{to}")
    public Response postCrow(@PathParam("from") String from, @PathParam("to") String to) {
        try {
            return getResponseCrow(from, to);
        } catch (NoCityInDatabaseException e) {
            return Response.status(400).entity("coordinates of these cities are not available").build();
        }
    }

    private Response getResponseCrow(String from, String to) throws NoCityInDatabaseException {
        Integer result = -1;
        result = CalculationService.getCrow(from, to);
        return Response.status(200).entity(result.toString()).build();
    }

    @GET
    @Path("all/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("from") String from, @PathParam("to") String to) {
        Map<String, Integer> result = new HashMap<>();
        try {
            result.put("Crowflight", (Integer) getResponseCrow(from, to).getEntity());
            result.put("Distance Matrix", (Integer) getResponseDistance(from, to).getEntity());
        } catch (NoCityInDatabaseException e) {
            return Response.status(400).entity("coordinates of these cities are not available").build();
        }
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("all/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAll(@PathParam("from") String from, @PathParam("to") String to) {
        Map<String, Integer> result = new HashMap<>();
        try {
            result.put("Crowflight", (Integer) getResponseCrow(from, to).getEntity());
            result.put("Distance Matrix", (Integer) getResponseDistance(from, to).getEntity());
        } catch (NoCityInDatabaseException e) {
            return Response.status(400).entity("coordinates of these cities are not available").build();
        }
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("get/{type}/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType(@PathParam("type") String type, @PathParam("from") String from, @PathParam("to") String to) {
        return getResponseAll(type, from, to);
    }

    @POST
    @Path("get/{type}/{from}/{to}")
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
