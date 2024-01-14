package src.excep;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ResourceNotFoundException extends WebApplicationException {

    public ResourceNotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity(new Error(message))
                .type(MediaType.APPLICATION_XML)
                .build());
    }
}