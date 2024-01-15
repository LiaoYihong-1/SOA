package com.example.server2;

import com.example.server2.model.*;
import com.example.server2.model.Error;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



import javax.naming.NamingException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Path("/hr")
public class HrResource {

    HelloWorld helloWorldEjb = JNDIConfig.helloWorldBean();

    public HrResource() throws NamingException {
    }

    // java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.management/javax.management=ALL-UNNAMED --add-opens=java.management/javax.management.openmbean=ALL-UNNAMED -jar ../t2/payara-micro-6.2023.11.jar --ssLPort 9090 --deploy server2.war
    // java -jar ../t2/payara-micro-5.2022.5.jar --ssLPort 9090 --deploy server2.war
    @GET
    @Path("/test")
    public void fire() {
        System.out.println("test_ejb");
        System.out.println(helloWorldEjb.getHelloWorld());
    }

    private Client createConfiguredClient() throws Exception {
        char[] password = "123456".toCharArray();
        KeyStore keystore = KeyStore.getInstance("PKCS12");

        try (InputStream keystoreInputStream = getClass().getResourceAsStream("../../../keystore.jks")) {
            keystore.load(keystoreInputStream, password);
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        return ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier((hostname, session) -> true).build();
    }

    @DELETE
    @Path("/fire/{id}")
    @Produces("application/xml")
    public Response fire(@PathParam(value = "id") Integer id){
        try {
            Client client = createConfiguredClient();

            String springServiceUrl = "https://localhost:9000/company/workers/" + id.toString();

            Response response = client.target(springServiceUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                Worker worker = response.readEntity(Worker.class);

                String moveUrl = "https://localhost:9000/company/workers/" + id;

                worker.setOrganization(null);


                client.target(moveUrl)
                        .request(MediaType.APPLICATION_XML)
                        .put(Entity.entity(WorkerInfo.ConvertWorker(worker), MediaType.APPLICATION_XML));

                return Response.status(Response.Status.OK)
                        .entity(worker)
                        .build();
            } else {
                Error e1 = new Error();
                e1.setMessage("Invalid request");
                e1.setCode(400);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(e1)
                        .build();
            }
        }catch (NotFoundException notFoundException){
            Error e2 = new Error();
            e2.setCode(400);
            e2.setMessage("Invalid request");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e2)
                    .build();
        }
        catch (Exception e) {
            Error e2 = new Error();
            e2.setCode(500);
            e2.setMessage("Internal server error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e2)
                    .build();
        }
    }

    @PUT
    @Path("/move/{worker-id}/{id-from}/{id-to}")
    @Produces("application/xml")
    public Response move(@PathParam(value = "worker-id") Integer workerId,
                         @PathParam(value = "id-from") Integer idFrom,
                         @PathParam(value = "id-to") Integer idTo){
        try {

            Client client = createConfiguredClient();


            String springServiceUrl = "https://localhost:9000/company/workers/" + workerId.toString();


            Response response = client.target(springServiceUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();

            String organizationToUrl = "https://localhost:9000/company/organization/" + idTo.toString();
            String organizationFromUrl = "https://localhost:9000/company/organization/" + idFrom.toString();
            Response response1 = client.target(organizationToUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();
            Response response2 = client.target(organizationFromUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode() &&
                    response1.getStatus() == Response.Status.OK.getStatusCode() &&
                    response2.getStatus() == Response.Status.OK.getStatusCode()) {
                Worker worker = response.readEntity(Worker.class);
                System.out.println(worker.getStartDate());
                Organization organizationTo = response1.readEntity(Organization.class);
                Organization organizationFrom = response2.readEntity(Organization.class);
                if(!organizationFrom.getId().equals(worker.getOrganization().getId())){
                    Error e = new Error();
                    e.setMessage("Invalid request");
                    e.setCode(400);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(e)
                            .build();
                }
                String moveUrl = "https://localhost:9000/company/workers/" + workerId;
                worker.setOrganization(organizationTo);
                client.target(moveUrl)
                        .request(MediaType.APPLICATION_XML)
                        .put(Entity.entity(WorkerInfo.ConvertWorker(worker), MediaType.APPLICATION_XML));
                return Response.status(Response.Status.OK)
                        .entity(worker)
                        .build();
            } else {
                Error e1 = new Error();
                e1.setMessage("Invalid request");
                e1.setCode(400);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(e1)
                        .build();
            }
        }catch (NotFoundException notFoundException){
            Error e2 = new Error();
            e2.setCode(400);
            e2.setMessage("Invalid request");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e2)
                    .build();
        }
        catch (Exception e) {
            Error e2 = new Error();
            e2.setCode(500);
            e2.setMessage("Internal server error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e2)
                    .build();
        }
    }
}
