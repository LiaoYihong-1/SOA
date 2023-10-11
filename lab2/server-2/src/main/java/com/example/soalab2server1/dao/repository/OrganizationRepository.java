package com.example.soalab2server1.dao.repository;

import com.example.soalab2server1.dao.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    @Override
    Optional<Organization> findById(Integer integer);
}
