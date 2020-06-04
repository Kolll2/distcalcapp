package com.kolll.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/cities")
public class Cities {

    @GET
    @Path("")
    public Response printCities() {
        return Response.status(Response.Status.ACCEPTED).entity("Cities list").build();
    }
}
