package src.service.impl;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.jboss.ejb3.annotation.Pool;
import src.dao.model.Organization;
import src.dao.repository.OrganizationRepI;
import src.excep.ResourceNotFoundException;
import src.service.operation.OrganizationOperation;

@Stateless
@Pool(value = "i-hate-ejb")
public class OrganizationService implements OrganizationOperation {
    @Inject
    private OrganizationRepI organizationRepository;
    @Override
    public Organization getOrgan(Integer id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        System.out.println(organization.toString());
        return organization;
    }
}
