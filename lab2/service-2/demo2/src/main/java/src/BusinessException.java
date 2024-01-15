package src;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class BusinessException extends WebApplicationException {

    public BusinessException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new Error(message))
                .type(MediaType.APPLICATION_XML)
                .build());
    }
}