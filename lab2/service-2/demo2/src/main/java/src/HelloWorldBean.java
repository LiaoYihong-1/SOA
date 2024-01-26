package src;


import jakarta.ejb.Stateless;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.ejb3.annotation.Pool;
import src.model.Organization;
import src.model.Worker;
import src.model.Error;
import src.model.WorkerInfo;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Stateless
@Pool(value = "i-hate-ejb")
public class HelloWorldBean implements HelloWorld {
    @Override
    public String getHelloWorld() {
        return "Welcome to EJB Tutorial!";
    }

    private Client createConfiguredClient() throws Exception {
        char[] password = "123456".toCharArray();
        KeyStore keystore = KeyStore.getInstance("PKCS12");

        try (InputStream keystoreInputStream = getClass().getResourceAsStream("../../keystore.jks")) {
            keystore.load(keystoreInputStream, password);
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        return ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier((hostname, session) -> true).build();
    }

    @Override
    public void fireWorker(Integer id) {
        try {
            System.out.println("[INFO] fireWorker");
            Client client = createConfiguredClient();
            System.out.println("[INFO] createConfiguredClient success");
            String springServiceUrl = "https://localhost:9999/company/workers/" + id.toString();

            Response response = client.target(springServiceUrl)
                    .request(MediaType.APPLICATION_XML)
                    .get();
            System.out.println("[INFO] client.target(springServiceUrl)");
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("[INFO] response.getStatus()");

                Worker worker = response.readEntity(Worker.class);;
                if (worker.getOrganization().getId()==null)
                    throw new NotFoundException("Invalid request");
                String moveUrl = "https://localhost:9999/company/workers/" + id;
                System.out.println("[INFO] moveUrl");

                worker.setOrganization(null);

                client.target(moveUrl)
                        .request(MediaType.APPLICATION_XML)
                        .put(Entity.entity(WorkerInfo.ConvertWorker(worker), MediaType.APPLICATION_XML));

            } else {
                throw new NotFoundException("Invalid request");
            }
        } catch (NotFoundException notFoundException) {
            System.out.println("ex1");
            throw new NotFoundException("Invalid request");
        } catch (Exception e) {
            System.out.println("ex2");
            throw new BusinessException("Internal server error");
        }
    }

    @Override
    public Worker move(Integer workerId, Integer idFrom, Integer idTo) throws Exception {
        Client client = createConfiguredClient();
        String springServiceUrl = "https://localhost:9999/company/workers/" + workerId.toString();


        Response response = client.target(springServiceUrl)
                .request(MediaType.APPLICATION_XML)
                .get();

        String organizationToUrl = "https://localhost:9999/company/organization/" + idTo.toString();
        String organizationFromUrl = "https://localhost:9999/company/organization/" + idFrom.toString();
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
            Organization organizationTo = response1.readEntity(Organization.class);
            Organization organizationFrom = response2.readEntity(Organization.class);
            if (!organizationFrom.getId().equals(worker.getOrganization().getId())) {
                Error e = new Error();
                e.setMessage("Invalid request");
                e.setCode(400);
                return null;
            }
            String moveUrl = "https://localhost:9999/company/workers/" + workerId;
            worker.setOrganization(organizationTo);
            client.target(moveUrl)
                    .request(MediaType.APPLICATION_XML)
                    .put(Entity.entity(WorkerInfo.ConvertWorker(worker), MediaType.APPLICATION_XML));
            return worker;
        }
        return null;
    }
}