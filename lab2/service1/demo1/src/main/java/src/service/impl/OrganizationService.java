package src.service.impl;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import src.dao.model.Organization;
import src.dao.repository.OrganizationRepI;
import src.excep.ResourceNotFoundException;
import src.service.operation.OrganizationOperation;

@Stateless
public class OrganizationService implements OrganizationOperation {
    @Inject
    private OrganizationRepI organizationRepository;
    @Override
    public Response getOrgan(Integer id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        return Response.ok(organization).build();
    }
}
