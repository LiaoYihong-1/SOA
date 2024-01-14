package src.service.operation;

import jakarta.ejb.Remote;
import jakarta.ws.rs.core.Response;

@Remote
public interface OrganizationOperation {
     Response getOrgan(Integer id);
}
