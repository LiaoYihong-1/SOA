package com.example.soalab2server1.service.impl;

import com.example.soalab2server1.dao.model.Error;
import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.repository.OrganizationRepository;
import com.example.soalab2server1.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    public ResponseEntity<?> getOrgan(Integer id){
        Organization organization = organizationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(""));
        return ResponseEntity.ok(organization);
    }
}
