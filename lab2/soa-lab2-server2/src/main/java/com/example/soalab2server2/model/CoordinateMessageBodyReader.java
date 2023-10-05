package com.example.soalab2server2.model;

import javax.ws.rs.Produces;
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
@Produces(MediaType.APPLICATION_XML)
public class CoordinateMessageBodyReader implements MessageBodyReader<Coordinate> {
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Coordinate.class;
    }

    @Override
    public Coordinate readFrom(Class<Coordinate> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                                 MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Coordinate.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (Coordinate) unmarshaller.unmarshal(entityStream);
        } catch (JAXBException e) {
            throw new IOException("Failed to unmarshal XML into Coordinate object", e);
        }
    }
}
