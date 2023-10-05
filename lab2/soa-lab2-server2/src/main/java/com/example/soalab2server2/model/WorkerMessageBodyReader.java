package com.example.soalab2server2.model;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.bind.JAXBContext;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
@Provider
@Produces(MediaType.APPLICATION_XML)
public class WorkerMessageBodyReader implements MessageBodyReader<Worker> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Worker.class;
    }

    @Override
    public Worker readFrom(Class<Worker> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            // Create a JAXB context for Worker class
            JAXBContext jaxbContext = JAXBContext.newInstance(Worker.class);

            // Create an unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Unmarshal the XML data into a Worker object
            return (Worker) unmarshaller.unmarshal(entityStream);
        } catch (JAXBException e) {
            throw new IOException("Failed to unmarshal XML into Worker object", e);
        }
    }
}