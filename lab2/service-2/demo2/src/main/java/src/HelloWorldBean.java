package src;


import jakarta.ejb.Stateless;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import src.model.Organization;
import src.model.Worker;
import src.model.WorkerInfo;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Stateless
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

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                Worker worker = response.readEntity(Worker.class);

                String moveUrl = "https://localhost:9999/company/workers/" + id;

                worker.setOrganization(null);

                client.target(moveUrl)
                        .request(MediaType.APPLICATION_XML)
                        .put(Entity.entity(WorkerInfo.ConvertWorker(worker), MediaType.APPLICATION_XML));

            } else {
                throw new BusinessException("Invalid request");
            }
        }catch (NotFoundException notFoundException){
            throw new BusinessException("Invalid request");
        }
        catch (Exception e) {
            throw new BusinessException("Internal server error");
        }
    }

    @Override
    public Organization getOrganizationById(Integer organizationId) {
        return null;
    }

    @Override
    public void updateWorker(Worker worker) {

    }
}