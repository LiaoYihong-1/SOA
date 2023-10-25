package com.example.soalab2server1.controller;

import com.example.soalab2server1.dao.request.CreateWorkerRequest;
import com.example.soalab2server1.dao.request.WorkerInfo;
import com.example.soalab2server1.service.impl.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.List;
@Validated
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping(value = "/company/workers", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> addWorker(@Valid @RequestBody CreateWorkerRequest worker){
        return workerService.createWorker(worker);
    }

    @GetMapping(value ="/company/workers/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getWorker(@PathVariable @Min(0) Integer id){
        return workerService.getById(id);
    }

    @DeleteMapping(value ="/company/workers/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> deleteWorker(@PathVariable @Min(0) Integer id){
        return workerService.delete(id);
    }

    @GetMapping(value ="/company/workers/salary/max", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getMaxSalary(){
        return workerService.getByMaxSalary();
    }

    @PutMapping(value ="/company/workers/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> updateWorker(@RequestBody WorkerInfo w, @PathVariable @Min(0) Integer id) {
        return workerService.updateWorker(w, id);
    }

    @GetMapping(value ="/company/workers/count", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getAmountByEndDate(@RequestParam(required = false,value = "enddate") String endDate,
                                                @RequestParam(required = false,value = "condition") String condition)  throws InvalidParameterException {
        return workerService.getAmountByEndDate(endDate,condition);
    }

    @GetMapping(value ="/company/workers", produces = MediaType.APPLICATION_XML_VALUE)
    public com.example.soalab2server1.dao.model.Page<?> getWorkersBySortAndFilter(@RequestParam(required = false, value = "sortElements") List<String> sort,
                                             @RequestParam(required = false,value = "filter") List<String> filters,
                                             @RequestParam(required = false,value = "isUpper") Boolean isUpper,
                                             @RequestParam(required = false,value = "pageSize") @Min(1) Integer pageSize,
                                             @RequestParam(required = false,value = "page") @Min(0) Integer pageNum){
        return workerService.getList(sort, filters,isUpper,pageSize,pageNum);
    }


}
