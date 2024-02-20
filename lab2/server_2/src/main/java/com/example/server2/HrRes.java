package com.example.server2;

import com.example.server2.model.Worker;
import com.example.server2.model.WorkerInfo;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.ws.rs.core.Response;
import jakarta.xml.soap.SOAPException;

@WebService
public interface HrRes {
    @WebMethod
    Worker test(@WebParam(name = "id") int id);
    @WebMethod
    String fire(@WebParam(name = "id") int id) throws SOAPException;
    @WebMethod
    Worker move(@WebParam(name = "worker-id") int workerId,
                         @WebParam(name = "id-from") int idFrom,
                         @WebParam(name = "id-to") int idTo) throws SOAPException;
}