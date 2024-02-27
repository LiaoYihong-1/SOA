package com.example.server2;

import com.example.server2.model.Worker;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.soap.SOAPException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@WebService
public interface HrRes {
    @WebMethod
    Worker test();
    @WebMethod
    String fire(@WebParam(name = "id") String id) throws SOAPException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException;
    @WebMethod
    public String move(@WebParam(name = "id") String workerIdStr,
                       @WebParam(name = "id-from") String orgFromIdStr,
                       @WebParam(name = "id-to") String orgToIdStr) throws SOAPException ;
}
