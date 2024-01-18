package src.service.operation;

import jakarta.ejb.Remote;
import jakarta.ws.rs.core.Response;
import src.dao.model.Organization;

@Remote
public interface OrganizationOperation {
     Organization getOrgan(Integer id);
}
