package com.kolll.web;

import com.kolll.model.database.Database;
import com.kolll.model.entities.Distance;
import com.kolll.service.Calculation;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/distance")
public class Distances {

    @GET
    @Path("get/{from}/{to}")
    public Response getDistance(@PathParam("from") String from, @PathParam("to") String to){

//        перенести логику на уровень ниже
//        убрать дублирование с методом пост

        String result = null;
        try {
            result = Database.getInstance().getDistance(from, to);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result == null) return Response.status(500).entity("this route is not in the database").build();
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("get/{from}/{to}")
    public Response postDistance(@PathParam("from") String from, @PathParam("to") String to){
        String result = null;
        try {
            result = Database.getInstance().getDistance(from, to);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result == null) return Response.status(500).entity("this route is not in the database").build();
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("crow/{from}/{to}")
    public Response getCrow(@PathParam("from") String from, @PathParam("to") String to){
        return Response.status(200).entity(Calculation.getCrow(from, to).toString()).build();
    }

    @POST
    @Path("crow/{from}/{to}")
    public Response postCrow(@PathParam("from") String from, @PathParam("to") String to){
        return Response.status(200).entity(Calculation.getCrow(from, to).toString()).build();
    }

//    Тестовые данные, удалить

    @GET
    @Path("insert")
    public Response addDistance() {
        List<Distance> temp = new ArrayList<>();
        temp.add(new Distance(1L, 2L, 1062));
        temp.add(new Distance(2L, 1L, 1062));
        temp.add(new Distance(1L, 3L, 826));
        temp.add(new Distance(3L, 1L, 826));
        temp.add(new Distance(2L, 3L, 364));
        temp.add(new Distance(3L, 2L, 364));

        try {
            Database.getInstance().insertDistances(temp);
            return Response.status(200).entity("Distances added").build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(500).entity("Something went wrong").build();
    }

    @GET
    @Path("printdistance")
    public Response printDistance() {
        List result = null;
        try {
            result = Database.getInstance().getDistances();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result == null) return Response.status(500).entity("Distance DB is empty").build();
        return Response.status(200).entity(Arrays.toString(result.toArray())).build();
    }

}
