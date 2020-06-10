package com.kolll.web;

import com.kolll.service.DataAccessService;
import com.kolll.service.DataUploadService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
        } else if (type.equals("update"))
            if (DataUploadService.update(tempFile)) {
                return Response.status(200).build();
            }
        return Response.status(400).build();
    }

}
