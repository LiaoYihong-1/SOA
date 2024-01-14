package src;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import src.excep.ResourceNotFoundException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception ex) {
        String errorMessage = "Invalid request";
        int httpStatus = Response.Status.BAD_REQUEST.getStatusCode();

        if (ex instanceof ResourceNotFoundException) {
            errorMessage = "The specified resource was not found";
            httpStatus = Response.Status.NOT_FOUND.getStatusCode();
        }

        Error message = new Error();
        message.setCode(httpStatus);
        message.setMessage(errorMessage);

        return Response.status(httpStatus)
                .entity(message)
                .type(MediaType.APPLICATION_XML)
                .build();
    }
}