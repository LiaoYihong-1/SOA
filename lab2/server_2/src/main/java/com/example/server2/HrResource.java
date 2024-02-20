package com.example.server2;

import com.example.server2.model.*;
import com.example.server2.model.Error;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.namespace.QName;
import java.io.InputStream;
import java.security.KeyStore;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebService
@Slf4j
@RequiredArgsConstructor
public class HrResource implements HrRes {


    @Override
    public Worker test(@WebParam(name = "id") int id) {
        System.out.println("id=" + id);
        Worker w = new Worker();
        w.setId(1);
        w.setName("asdf");
        w.setCoordinate(new Coordinate(2L, 2));
        w.setCreationDate(LocalDateTime.now());
        w.setSalary(9);
        w.setPosition(Position.COOK);
        w.setOrganization(new Organization(1, "Google", 654654L));
        w.setStartDate(LocalDate.now());
        w.setEndDate(LocalDate.now());
        return w;
    }

    @Override
    public WorkerInfo fire(@WebParam(name = "worker") Worker worker) throws SOAPException {
        try {
            if (worker.getOrganization().getId() == null)
                throw new NotFoundException("Invalid request");
            System.out.println(worker);
            worker.setOrganization(null);

            return WorkerInfo.ConvertWorker(worker);

        } catch (NotFoundException notFoundException) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        } catch (Exception e) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "500,Internal server error",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
            throw new SOAPFaultException(soapFault);
        }
    }

    @Override
    public WorkerInfo move(@WebParam(name = "worker") Worker worker,
                           @WebParam(name = "org-from") Organization orgFrom,
                           @WebParam(name = "org-to") Organization orgTo) throws SOAPException {
        try {

            if (
                    !orgFrom.getId().equals(worker.getOrganization().getId())
                            || orgTo.getId().equals(worker.getOrganization().getId())
            ) {
                throw new NotFoundException("Invalid request");
            }

            worker.setOrganization(orgTo);

            return WorkerInfo.ConvertWorker(worker);

        } catch (NotFoundException notFoundException) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "400,Invalid request",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client"));
            throw new SOAPFaultException(soapFault);
        } catch (Exception e) {
            SOAPFactory soapFactory = SOAPFactory.newInstance();
            SOAPFault soapFault = soapFactory.createFault(
                    "500,Internal server error",
                    new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
            throw new SOAPFaultException(soapFault);
        }
    }
}
