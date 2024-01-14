package src.dao.repository;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import src.dao.model.Organization;

import java.util.List;

@ApplicationScoped
public class OrganizationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Organization findById(Integer id) {
        return entityManager.find(Organization.class, id);
    }

    public List<Organization> findAll() {
        return entityManager.createQuery("SELECT o FROM Organization o", Organization.class)
                .getResultList();
    }

    public Organization save(Organization organization) {
        entityManager.persist(organization);
        return organization;
    }

    public Organization update(Organization organization) {
        return entityManager.merge(organization);
    }

    public void delete(Long id) {
        Organization organization = findById(id);
        if (organization != null) {
            entityManager.remove(organization);
        }
    }
}
