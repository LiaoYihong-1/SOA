package com.example.server2;

import com.example.server2.exception.DefaultExceptionMapper;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class HelloApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JNDIConfig.class);
        classes.add(HrResource.class);
        classes.add(DefaultExceptionMapper.class);
        return classes;
    }
}