package com.example.soalab2server1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import src.service.operation.OrganizationOperation;
import src.service.operation.ServiceOperation;


@RestController
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationOperation organizationOperationOperation;
    private final ServiceOperation serviceOperation;

    @GetMapping(value = "/company/organization/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getOrganization(@PathVariable Integer id) {

        // todo
        //      return organizationOperationOperation.getOrgan(id);
        return null;
    }

    @GetMapping(value = "/company/organization", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getOrganization() {
        log.info(serviceOperation.hello());
        return null;
    }

}
