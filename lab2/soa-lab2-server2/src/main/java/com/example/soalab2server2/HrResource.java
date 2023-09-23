package com.example.soalab2server2;

import com.example.soalab2server2.model.Organization;
import com.example.soalab2server2.model.Worker;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
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

        // URL of the Spring endpoint
        String springServiceUrl = "http://localhost:8080/company/workers/" + workerId.toString();

        // Make a GET request and specify Accept header for JSON response
        Response response = client.target(springServiceUrl)
                .request(MediaType.APPLICATION_JSON)
                .get();
        //Organization
        String organizationToUrl = "http://localhost:8080/company/organization/" + idTo.toString();
        String organizationFromUrl = "http://localhost:8080/company/organization/" + idFrom.toString();
        Response response1 = client.target(organizationToUrl)
                .request(MediaType.APPLICATION_JSON)
                .get();
        Response response2 = client.target(organizationFromUrl)
                .request(MediaType.APPLICATION_JSON)
                .get();
        // Check the response status
        if (response.getStatus() == Response.Status.OK.getStatusCode() &&
                response1.getStatus() == Response.Status.OK.getStatusCode() &&
                response2.getStatus() == Response.Status.OK.getStatusCode()) {
            Worker worker = response.readEntity(Worker.class);
            Organization organization = response1.readEntity(Organization.class);
            String moveUrl = "http://localhost:8080/company/workers/" + workerId.toString();
            worker.setOrganization(organization);
            Response finalResponse = client.target(moveUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(worker,MediaType.APPLICATION_JSON));
            return Response.status(Response.Status.OK)
                    .entity(worker)
                    .build();
        } else {
            // Handle error responses if needed
            Error e;
            System.err.println("Error: " + response.getStatusInfo().getReasonPhrase());
            if(response1.getStatus() != Response.Status.OK.getStatusCode()){
                e = response1.readEntity(Error.class);
                return Response.status(Response.Status.NOT_FOUND).entity(e).build();
            }else if(response2.getStatus() != Response.Status.OK.getStatusCode()){
                e = response2.readEntity(Error.class);
                return Response.status(Response.Status.NOT_FOUND).entity(e).build();
            }else {
                e = response.readEntity(Error.class);
            }
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }
}
