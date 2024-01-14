package src.service.operation;

import jakarta.ejb.Remote;
import jakarta.ws.rs.core.Response;
import src.dao.model.NumberOfWorkers;
import src.dao.model.Page;
import src.dao.model.WorkerFullInfo;
import src.dao.request.CreateWorkerRequest;
import src.dao.request.WorkerInfo;

import java.security.InvalidParameterException;
import java.util.List;
@Remote
public interface ServiceOperation {
    WorkerFullInfo createWorker(CreateWorkerRequest t);
    Page<?> getList(List<String> sort, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum);
    Response delete(Integer id);
    WorkerFullInfo updateWorker(WorkerInfo t, Integer id);
    NumberOfWorkers getAmountByEndDate(String endDate, String condition) throws InvalidParameterException;
    WorkerFullInfo getByMaxSalary();
    WorkerFullInfo getById(Integer id);
    String hello();
}
