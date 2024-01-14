package src.service.impl;


import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import src.dao.model.Organization;
import src.dao.repository.OrganizationRepository;
import src.excep.ResourceNotFoundException;

@Stateless
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    public Response getOrgan(Integer id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        return Response.ok(organization).build();
    }
}
