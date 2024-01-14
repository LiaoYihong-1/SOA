package src.dao.repository;

import src.dao.model.Organization;

import java.util.Optional;

public interface OrganizationRepI {
     Optional<Organization> findById(Integer id);
     boolean existsById(Integer id) ;

}
