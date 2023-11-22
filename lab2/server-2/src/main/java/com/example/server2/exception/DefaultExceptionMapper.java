package com.example.server2.exception;


import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.example.server2.model.Error;
import lombok.extern.slf4j.Slf4j;

@Provider @Slf4j
public class DefaultExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        log.info(exception.getMessage());
        Error e = new Error();
        e.setMessage("Http method is not supported");
        e.setCode(405);
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .entity(e)
                .type("text/xml")
                .build();
    }
}