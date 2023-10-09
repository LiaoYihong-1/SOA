package com.example.soalab2server2.model;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(MediaType.APPLICATION_XML)
public class GenericMessageBodyReader implements MessageBodyReader<Object> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           javax.ws.rs.core.MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException {
        // Implement logic to read and deserialize the XML into an object
        // You can use JAXB or any other XML parsing library here
        // For simplicity, let's assume Worker class

        WorkerMessageBodyReader workerReader = new WorkerMessageBodyReader();

        if (workerReader.isReadable(Worker.class, Worker.class, annotations, mediaType)) {
            return workerReader.readFrom(Worker.class, Worker.class, annotations, mediaType, httpHeaders, entityStream);
        }

        // If it's not a Worker class, handle accordingly (add other cases as needed)
        return null;
    }
}