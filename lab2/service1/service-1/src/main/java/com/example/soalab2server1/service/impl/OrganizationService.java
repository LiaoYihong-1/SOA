//package com.example.soalab2server1.service.impl;
//
//import com.example.soalab2server1.dao.model.Organization;
//import com.example.soalab2server1.dao.repository.OrganizationRepository;
//import com.example.soalab2server1.exception.ResourceNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@RequiredArgsConstructor
//public class OrganizationService {
//    private final OrganizationRepository organizationRepository;
//
//    public ResponseEntity<?> getOrgan(Integer id) {
//        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
//        return ResponseEntity.ok(organization);
//    }
//}
