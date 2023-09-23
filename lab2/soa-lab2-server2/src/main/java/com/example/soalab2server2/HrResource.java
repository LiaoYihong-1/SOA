package com.example.soalab2server2;

import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hr")
public class HrResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
    @DELETE
    @Path("/fire/{id}")
    public Response fire(@PathParam(value = "id") Integer id){
        // Create a JAX-RS client
        Client client = ClientBuilder.newClient();

        // Define the URL of the Spring service
        String springServiceUrl = "http://localhost:8080/company/workers/" + id.toString();

        // Make a GET request to the Spring service
        String response = client.target(springServiceUrl)
                .request(MediaType.APPLICATION_JSON)
                .delete(String.class);

        // Close the JAX-RS client
        client.close();

        // Build a response and return it
        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }

    @PUT
    @Path("/move/{worker-id}/{id-from}/{id-to}")
    public Response move(@PathParam(value = "worker-id") Integer workerId,
                         @PathParam(value = "id-from") Integer idFrom,
                         @PathParam(value = "id-to") Integer idTo){
        // Create a JAX-RS client
        Client client = ClientBuilder.newClient();

        // Define the URL of the Spring service
        String springServiceUrl = "http://localhost:8080/company/workers/" + ;

        // Make a GET request to the Spring service
        String response = client.target(springServiceUrl)
                .request(MediaType.APPLICATION_JSON)
                .delete(String.class);

        // Close the JAX-RS client
        client.close();

        // Build a response and return it
        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }
}
