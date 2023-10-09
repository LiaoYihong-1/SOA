package com.example.soalab2server2.model;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
@Consumes("application/xml")
public class WorkerMessageBodyReader implements MessageBodyReader<Worker> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Worker readFrom(Class<Worker> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Worker.class);
            return (Worker) jaxbContext.createUnmarshaller().unmarshal(entityStream);
        } catch (JAXBException e) {
            throw new IOException("Failed to unmarshal XML into Worker object", e);
        }
    }
}