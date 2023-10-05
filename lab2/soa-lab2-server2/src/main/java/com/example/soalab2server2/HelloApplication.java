package com.example.soalab2server2;

import com.example.soalab2server2.model.CoordinateMessageBodyReader;
import com.example.soalab2server2.model.OrganizationMessageBodyReader;
import com.example.soalab2server2.model.WorkerMessageBodyReader;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {

}