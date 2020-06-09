package com.kolll.web;

import com.kolll.service.DataAccessService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/cities")
public class Cities {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer, String> printCities() {
        return DataAccessService.getCities();
    }
}
