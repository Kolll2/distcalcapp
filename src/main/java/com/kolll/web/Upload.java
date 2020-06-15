package com.kolll.web;

import com.kolll.model.entities.City;
import com.kolll.model.entities.Distance;
import com.kolll.service.DataAccessService;
import com.kolll.service.DataUploadService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

@Path("/upload")
public class Upload {

    @POST
    @Path("/{type}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(File file, @PathParam("type") String type) {
        File tempFile = new File("temp.xml");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile))) {
                int count = 0;
                while (true) {
                    String line = bufferedReader.readLine();
                    if (count++ <= 4) continue;
                    if (!bufferedReader.ready()) break;
                    bufferedWriter.write(line + System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (type.equals("upload")) {
            if (DataUploadService.upload(tempFile)) {
                return Response.status(200).build();
            }
        } else if (type.equals("update")) {
            if (DataUploadService.update(tempFile)) {
                return Response.status(200).build();
            }
        } else if (type.equals("unification")){
            if (DataUploadService.unification(tempFile)) {
                return Response.status(200).build();
            }
        }
//        else if (type.equals("tests")){
//            if (DataUploadService.unificationStringData(tempFile)) {
//                return Response.status(200).build();
//            }
//        }
        return Response.status(400).build();
    }

    @GET
    @Path("/addcity/{name}/{latitude}/{longitude}")
    public Response addCity(@PathParam("name") String name, @PathParam("latitude") Float latitude, @PathParam("longitude") Float longitude) {
        DataUploadService.mergerCities(new City(name, latitude, longitude));
        return Response.status(200).build();
    }

    @GET
    @Path("/adddistance/{from}/{to}/{distance}")
    public Response addDistance(@PathParam("from") String from, @PathParam("to") String to, @PathParam("distance") Integer distance) {


        DataUploadService.mergingDistances(new Distance(DataAccessService.getCityIdByName(from),
                DataAccessService.getCityIdByName(to), distance));
        return Response.status(200).build();
    }
}
