package com.example.soalab2server1.service.impl;

import com.example.soalab2server1.dao.model.Error;
import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;

    public ResponseEntity<?> getOrgan(Integer id){
        Optional<Organization> organization = organizationRepository.findById(id);
        if (organization.isPresent()) {
            return ResponseEntity.ok(organization.get());
        } else {
            Error e = new Error();
            e.setMessage("The specified resource is not found");
            e.setCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

}
