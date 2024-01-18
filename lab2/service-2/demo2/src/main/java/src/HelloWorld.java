package src;


import jakarta.ejb.Remote;
import src.model.*;

@Remote
public interface HelloWorld {
    String getHelloWorld();
    void fireWorker(Integer workerId);
    Worker move (Integer workerId,Integer idFrom,Integer idTo) throws Exception;
}