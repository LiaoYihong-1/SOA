package com.example.soalab2server1.controller;

import com.example.soalab2server1.service.impl.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Although in swagger not this controller, we need it to finish lab.
 * Problem is in the description of first lab not defined id of organization but in second server need to use it.
 * So all classes about organization is created only for dealing with lab.
 */
@RestController
public class OrganizationController {
    @Autowired
    OrganizationService service;
    @GetMapping(value ="/company/organization/{id}")
    public ResponseEntity<?> getOrganization(@PathVariable Integer id){
        return service.getOrgan(id);
    }
}
