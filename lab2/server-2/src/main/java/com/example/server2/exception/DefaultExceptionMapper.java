package com.example.server2.exception;

import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.example.server2.model.Error;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        Error e = new Error();
        e.setMessage("Http method is not supported");
        e.setCode(405);
        if (exception instanceof NotFoundException || exception instanceof NotAcceptableException) {
            // 如果是 NotFound 或 NotAcceptable 异常，返回特定的拒绝响应
            return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                    .entity(e)
                    .type("text/xml")
                    .build();
        } else {
            // 其他情况返回通用拒绝响应
            return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                    .entity(e)
                    .type("text/xml")
                    .build();
        }
    }
}