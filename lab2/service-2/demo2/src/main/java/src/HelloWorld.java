package src;


import jakarta.ejb.Remote;
import src.model.*;

@Remote
public interface HelloWorld {
    String getHelloWorld();
    void fireWorker(Integer workerId);
    Organization getOrganizationById(Integer organizationId);
    void updateWorker(Worker worker);
}