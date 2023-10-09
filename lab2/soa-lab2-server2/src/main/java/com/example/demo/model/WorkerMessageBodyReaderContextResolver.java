package com.example.demo.model;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
public class WorkerMessageBodyReaderContextResolver implements ContextResolver<MessageBodyReader<?>> {

    @Override
    public MessageBodyReader<?> getContext(Class<?> type) {
        if (type == Worker.class) {
            return new WorkerMessageBodyReader();
        }
        return null;
    }
}