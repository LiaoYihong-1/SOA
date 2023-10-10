package com.example.server2;

import com.example.server2.model.*;
import com.example.server2.model.Error;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Path("/hr")
public class HrResource {

    @DELETE
    @Path("/fire/{id}")
    @Consumes({"application/json", "application/xml"})
    @Produces("application/xml")
    public Response fire(@PathParam(value = "id") Integer id){
        try {
            // Load the keystore
            char[] password = "123456".toCharArray();
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            try (InputStream keystoreInputStream = getClass().getResourceAsStream("../../../certificate1.p12")) {
                keystore.load(keystoreInputStream, password);
            }

            // Create and configure the SSLContext
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Create a JAX-RS client with the SSLContext
            Client client = ClientBuilder.newBuilder().sslContext(sslContext)
                    .hostnameVerifier((hostname, session) -> true).build();


            // Define the URL of the Spring service
            String springServiceUrl = "https://127.0.0.1:9000/company/workers/" + id.toString();

            // Make a GET request to the Spring service
            String response = client.target(springServiceUrl)
                    .request(MediaType.APPLICATION_XML)
                    .delete(String.class);

            // Close the JAX-RS client
            client.close();

            // Build a response and return it
            return Response.status(Response.Status.OK)
                    .entity(response)
                    .build();
        }catch (Exception e) {
            Error e1 = new Error();
            e1.setCode(500);
            e1.setMessage(e.getMessage());
            return Response.status(500).entity(e1).build();
        }
    }

    @PUT
    @Path("/move/{worker-id}/{id-from}/{id-to}")
    @Produces("application/xml")
    public Response move(@PathParam(value = "worker-id") Integer workerId,
                         @PathParam(value = "id-from") Integer idFrom,
                         @PathParam(value = "id-to") Integer idTo){
        try {
            // Create a JAX-RS client
            // Load the keystore
            char[] password = "123456".toCharArray();
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            try (InputStream keystoreInputStream = getClass().getResourceAsStream("../../../certificate1.p12")) {
                keystore.load(keystoreInputStream, password);
            }

            // Create and configure the SSLContext
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Create a JAX-RS client with the SSLContext
            Client client = ClientBuilder.newBuilder().sslContext(sslContext)
                    .hostnameVerifier((hostname, session) -> true).build();
            // URL of the Spring endpoint
            String springServiceUrl = "https://localhost:9000/company/workers/" + workerId.toString();

            // Make a GET request and specify Accept header for JSON response
            Response response = client.target(springServiceUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();
            //Organization
            String organizationToUrl = "https://localhost:9000/company/organization/" + idTo.toString();
            String organizationFromUrl = "https://localhost:9000/company/organization/" + idFrom.toString();
            Response response1 = client.target(organizationToUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();
            Response response2 = client.target(organizationFromUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();
            // Check the response status
            if (response.getStatus() == Response.Status.OK.getStatusCode() &&
                    response1.getStatus() == Response.Status.OK.getStatusCode() &&
                    response2.getStatus() == Response.Status.OK.getStatusCode()) {
                /*                GenericType<String> genericType = new GenericType<>() {};
                String organization1 = response1.readEntity(genericType);
                System.out.println("Received response entity: " + organization1);
                GenericType<String> genericType1= new GenericType<>() {};
                String worker1 = response.readEntity(genericType);
                System.out.println("Received response entity: " + worker1);
*/
                System.out.println("No error happened");
                Worker worker = response.readEntity(Worker.class);
                System.out.println("READ WORKER");
                System.out.println(worker.getCoordinate().toString());
                Organization organization = response1.readEntity(Organization.class);
                String moveUrl = "https://localhost:9000/company/workers/" + workerId.toString();
                worker.setOrganization(organization);
                Response finalResponse = client.target(moveUrl)
                        .request(MediaType.APPLICATION_XML)
                        .put(Entity.entity(worker, MediaType.APPLICATION_JSON));
                return Response.status(Response.Status.OK)
                        .entity(worker)
                        .build();
            } else {
                // Handle error responses if needed
                Error e;
                System.err.println("Error: " + response.getStatusInfo().getReasonPhrase());
                if (response1.getStatus() != Response.Status.OK.getStatusCode()) {
                    e = response1.readEntity(Error.class);
                    return Response.status(Response.Status.NOT_FOUND).entity(e).build();
                } else if (response2.getStatus() != Response.Status.OK.getStatusCode()) {
                    e = response2.readEntity(Error.class);
                    return Response.status(Response.Status.NOT_FOUND).entity(e).build();
                } else {
                    e = response.readEntity(Error.class);
                }
                return Response.status(Response.Status.NOT_FOUND).entity(e).build();
            }
        }catch (Exception e) {
            Error e1 = new Error();
            e1.setCode(500);
            e1.setMessage(e.getMessage());
            return Response.status(500).entity(e1).build();
        }
    }
}
