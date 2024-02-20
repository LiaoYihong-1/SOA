package com.example.server2;

import com.example.server2.model.Organization;
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
    Worker move(@WebParam(name = "worker") Worker worker,
                         @WebParam(name = "org-from") Organization orgFrom,
                         @WebParam(name = "org-to") Organization orgTo) throws SOAPException;
}
