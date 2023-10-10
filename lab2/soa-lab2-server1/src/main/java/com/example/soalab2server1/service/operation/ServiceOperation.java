package com.example.soalab2server1.service.operation;

import com.example.soalab2server1.dao.model.Worker;
import org.springframework.http.ResponseEntity;

import java.security.InvalidParameterException;
import java.util.List;

public interface ServiceOperation<T> {
    public ResponseEntity<?> post(T t);
    public ResponseEntity<?> getList(List<String> sort,List<String> filters,Boolean isUpper,Integer pageSize,Integer pageNum);

    public ResponseEntity<?> delete(Integer id);

    public ResponseEntity<?> put(T t, Integer id);

    public ResponseEntity<?> getAmountByEndDate(String endDate, String condition) throws InvalidParameterException;

    public ResponseEntity<?> getByMaxSalary();

    public ResponseEntity<?> getById(Integer id);
}
