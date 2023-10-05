package com.example.soalab2server2;

import com.example.soalab2server2.model.Organization;
import com.example.soalab2server2.model.Worker;
import com.example.soalab2server2.model.Error;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Path("/hr")
public class HrResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
/*
    @DELETE
    @Path("/fire/{id}")
    public Response fire(@PathParam(value = "id") Integer id){
            // Create a JAX-RS client
            Client client = ClientBuilder.newClient();

            // Define the URL of the Spring service
            //String springServiceUrl = "https://localhost:9000/company/workers/" + id.toString();
            String springServiceUrl = "http://localhost:8080/company/workers/" + id.toString();

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
    }
*/


    @DELETE
    @Path("/fire/{id}")
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
                System.out.println("No error happened");
                Worker worker = response.readEntity(Worker.class);
                Organization organization = response1.readEntity(Organization.class);
                String moveUrl = "https://localhost:9000/company/workers/" + workerId.toString();
                worker.setOrganization(organization);
                Response finalResponse = client.target(moveUrl)
                        .request(MediaType.APPLICATION_XML)
                        .put(Entity.entity(worker, MediaType.APPLICATION_XML));
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
