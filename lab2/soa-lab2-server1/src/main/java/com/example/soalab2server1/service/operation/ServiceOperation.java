package com.example.soalab2server1.service.operation;

import com.example.soalab2server1.dao.model.Worker;
import com.example.soalab2server1.dao.request.CreateWorkerRequest;
import com.example.soalab2server1.dao.request.UpdateWorkerRequest;
import org.springframework.http.ResponseEntity;

import java.security.InvalidParameterException;
import java.util.List;

public interface ServiceOperation<T> {
    ResponseEntity<?> post(CreateWorkerRequest t);

    ResponseEntity<?> getList(List<String> sort, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum);

    ResponseEntity<?> delete(Integer id);

    ResponseEntity<?> put(UpdateWorkerRequest t, Integer id);

    ResponseEntity<?> getAmountByEndDate(String endDate, String condition) throws InvalidParameterException;

    ResponseEntity<?> getByMaxSalary();

    ResponseEntity<?> getById(Integer id);
}
