package src.dao.repository;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import src.dao.model.Organization;
import java.util.Optional;
import java.util.List;

@ApplicationScoped
public class OrganizationRepository implements OrganizationRepI  {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Organization> findById(Integer id) {
        Organization organization =  entityManager.find(Organization.class, id);
        return Optional.ofNullable(organization);
    }
    public boolean existsById(Integer id) {
        return entityManager.find(Organization.class, id) != null;
    }
}
