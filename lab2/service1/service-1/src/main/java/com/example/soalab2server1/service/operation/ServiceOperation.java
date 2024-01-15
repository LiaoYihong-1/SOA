//package com.example.soalab2server1.service.operation;
//
//import com.example.soalab2server1.dao.request.CreateWorkerRequest;
//import com.example.soalab2server1.dao.request.WorkerInfo;
//import org.springframework.http.ResponseEntity;
//
//import java.security.InvalidParameterException;
//import java.util.List;
//
//public interface ServiceOperation<T> {
//    ResponseEntity<?> createWorker(CreateWorkerRequest t);
//
//    com.example.soalab2server1.dao.model.Page<?> getList(List<String> sort, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum);
//
//    ResponseEntity<?> delete(Integer id);
//
//    ResponseEntity<?> updateWorker(WorkerInfo t, Integer id);
//
//    ResponseEntity<?> getAmountByEndDate(String endDate, String condition) throws InvalidParameterException;
//
//    ResponseEntity<?> getByMaxSalary();
//
//    ResponseEntity<?> getById(Integer id);
//}
