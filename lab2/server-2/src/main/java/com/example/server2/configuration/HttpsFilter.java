package com.example.server2.configuration;

import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
@Provider
public class HttpsFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws NotAcceptableException,IOException {
        UriInfo uriInfo = requestContext.getUriInfo();

        // Check if the request is using HTTPS
        if (!uriInfo.getRequestUri().getScheme().equals("https")) {
            // Reject the request if not using HTTPS
            throw new NotAcceptableException("");
        }
    }
}
