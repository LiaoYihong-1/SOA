package com.example.soalab2server1.controller;

import com.example.soalab2server1.dao.model.Worker;
import com.example.soalab2server1.dao.request.CreateWorkerRequest;
import com.example.soalab2server1.service.impl.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
@Validated
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping(value = "/company/workers", produces = MediaType.APPLICATION_XML_VALUE,consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> addWorker(@Valid @RequestBody Worker worker){
        return workerService.post(worker);
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

    @PutMapping(value ="/company/workers/{id}", produces = MediaType.APPLICATION_XML_VALUE,consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> updateWorker(@RequestBody com.example.soalab2server1.dao.request.CreateWorkerRequest w, @PathVariable @Min(0) Integer id){
        log.info("sdf");
//        return workerService.put(w, id);
        return null;
    }

    @GetMapping(value ="/company/workers/count", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getAmountByEndDate(@RequestParam(required = false,value = "enddate") String endDate,
                                                @RequestParam(required = false,value = "condition") String condition)  throws InvalidParameterException {
        return workerService.getAmountByEndDate(endDate,condition);
    }

    @GetMapping(value ="/company/workers", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getWorkersBySortAndFilter(@RequestParam(required = false, value = "sortElement") List<String> sort,
                                                      @RequestParam(required = false,value = "filter") List<String> filters,
                                                      @RequestParam(required = false,value = "isUpper") Boolean isUpper,
                                                      @RequestParam(required = false,value = "pageSize") Integer pageSize,
                                                      @RequestParam(required = false,value = "page") Integer pageNum){
        return workerService.getList(sort, filters,isUpper,pageSize,pageNum);
    }


}
