package com.example.soalab2server2;

import com.example.soalab2server2.model.*;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(HrResource.class);
        classes.add(OrganizationMessageBodyReader.class);
        return classes;
    }
}