package com.example.soalab2server2.model;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private final JAXBContext context;

    public JAXBContextResolver() throws JAXBException {
        this.context = JAXBContext.newInstance(Worker.class); // Replace with your JAXB context initialization
    }

    @Override
    public JAXBContext getContext(Class<?> type) {
        return context;
    }
}