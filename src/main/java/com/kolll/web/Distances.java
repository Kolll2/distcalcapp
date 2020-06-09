package com.kolll.web;

import com.kolll.service.CalculationService;
import com.kolll.service.DataAccessService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/distance")
public class Distances {

    @GET
    @Path("get/{from}/{to}")
    public Response getDistance(@PathParam("from") String from, @PathParam("to") String to) {
        Integer result = DataAccessService.getDistance(from, to);
        if (result == null) return Response.status(500).entity("this route is not in the database").build();
        return Response.status(200).entity(result.toString()).build();
    }

    @POST
    @Path("get/{from}/{to}")
    public Response postDistance(@PathParam("from") String from, @PathParam("to") String to) {
        Integer result = DataAccessService.getDistance(from, to);
        if (result == null) return Response.status(500).entity("this route is not in the database").build();
        return Response.status(200).entity(result.toString()).build();
    }

    @GET
    @Path("crow/{from}/{to}")
    public Response getCrow(@PathParam("from") String from, @PathParam("to") String to) {
        Integer result = CalculationService.getCrow(from, to);
        if (result < 0) return Response.status(500).entity("the database returned invalid data").build();
        return Response.status(200).entity(result.toString()).build();

    }

    @POST
    @Path("crow/{from}/{to}")
    public Response postCrow(@PathParam("from") String from, @PathParam("to") String to) {
        Integer result = CalculationService.getCrow(from, to);
        if (result < 0) return Response.status(500).entity("the database returned invalid data").build();
        return Response.status(200).entity(result.toString()).build();
    }

    @GET
    @Path("all/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> getAll(@PathParam("from") String from, @PathParam("to") String to) {
        Map<String, Integer> result = new HashMap<>();

        result.put("Crowflight", CalculationService.getCrow(from, to));
        result.put("Distance Matrix", DataAccessService.getDistance(from, to));

        return result;
    }

    @POST
    @Path("all/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> postAll(@PathParam("from") String from, @PathParam("to") String to) {
        Map<String, Integer> result = new HashMap<>();

        result.put("Crowflight", CalculationService.getCrow(from, to));
        result.put("Distance Matrix", DataAccessService.getDistance(from, to));

        return result;
    }

    @GET
    @Path("get/{type}/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> getType(@PathParam("type") String type, @PathParam("from") String from, @PathParam("to") String to) {
        Map<String, Integer> result = new HashMap<>();

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
                result.put("ERROR", -1);
                break;
        }
        return result;
    }

    @POST
    @Path("post/{type}/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> postType(@PathParam("type") String type, @PathParam("from") String from, @PathParam("to") String to) {
        Map<String, Integer> result = new HashMap<>();

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
                result.put("ERROR", -1);
                break;
        }
        return result;
    }
}
