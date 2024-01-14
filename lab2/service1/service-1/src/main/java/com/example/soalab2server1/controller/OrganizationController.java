package com.example.soalab2server1.controller;

import com.example.soalab2server1.service.impl.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import src.HelloWorld;


@RestController
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService service;

    @GetMapping(value = "/company/organization/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getOrganization(@PathVariable Integer id) {
        return service.getOrgan(id);
    }


}
