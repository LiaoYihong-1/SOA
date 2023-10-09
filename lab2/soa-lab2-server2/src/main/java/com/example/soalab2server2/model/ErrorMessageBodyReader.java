package com.example.soalab2server2.model;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes("application/xml")
public class ErrorMessageBodyReader implements MessageBodyReader<Error> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Error.class;
    }

    @Override
    public Error readFrom(Class<Error> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                                 MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Error.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (Error) unmarshaller.unmarshal(entityStream);
        } catch (JAXBException e) {
            throw new IOException("Failed to unmarshal XML into Organization object", e);
        }
    }
}
