package com.example.soalab2server1.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import src.dao.model.*;
import src.dao.request.*;
import src.service.operation.ServiceOperation;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.InvalidParameterException;

@Validated
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class WorkerController {

    private final ServiceOperation workerService;

    @PostMapping(value = "/company/workers", produces = MediaType.APPLICATION_XML_VALUE)
    public WorkerFullInfo addWorker(@RequestBody @Valid CreateWorkerRequest worker) {
        return workerService.createWorker(worker);
    }

    @GetMapping(value = "/company/workers/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public WorkerFullInfo getWorker(@PathVariable @Min(0) Integer id) {
        return workerService.getById(id);
    }

    @DeleteMapping(value = "/company/workers/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> deleteWorker(@PathVariable @Min(0) Integer id) {
        workerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The worker was deleted successfully");
    }


    @GetMapping(value = "/company/workers/salary/max", produces = MediaType.APPLICATION_XML_VALUE)
    public WorkerFullInfo getMaxSalary() {
        return workerService.getByMaxSalary();
    }

    @PutMapping(value = "/company/workers/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public WorkerFullInfo updateWorker(@RequestBody @Valid WorkerInfo w, @PathVariable @Min(0) Integer id) {
        return workerService.updateWorker(w, id);
    }

    @GetMapping(value = "/company/workers/count", produces = MediaType.APPLICATION_XML_VALUE)
    public NumberOfWorkers getAmountByEndDate(
            @RequestParam(required = false, value = "enddate") String endDate,
            @RequestParam(required = false, value = "condition") String condition
    )throws InvalidParameterException {
        return workerService.getAmountByEndDate(endDate, condition);
    }

//    @GetMapping(value = "/company/workers", produces = MediaType.APPLICATION_XML_VALUE)
//    public com.example.soalab2server1.dao.model.Page<?> getWorkersBySortAndFilter(
//            @RequestParam(required = false, value = "sortElements") List<String> sort,
//            @RequestParam(required = false, value = "filter") List<String> filters,
//            @RequestParam(required = false, value = "isUpper") Boolean isUpper,
//            @RequestParam(required = false, value = "pageSize", defaultValue = "1") @Min(1) Integer pageSize,
//            @RequestParam(required = false, value = "page", defaultValue = "0") @Min(0) Integer pageNum
//    ) {
//        return workerService.getList(sort, filters, isUpper, pageSize, pageNum);
//    }


}
