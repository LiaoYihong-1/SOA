package com.example.soalab2server1.controller;

import com.example.soalab2server1.dao.model.Worker;
import com.example.soalab2server1.service.impl.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @PostMapping("/company/workers")
    public ResponseEntity<?> addWorker(@RequestBody Worker worker){
        return workerService.post(worker);
    }

    @GetMapping("/company/workers/{id}")
    public ResponseEntity<?> getWorker(@PathVariable Integer id){
        return workerService.getById(id);
    }

    @DeleteMapping("/company/workers/{id}")
    public ResponseEntity<?> deleteWorker(@PathVariable Integer id){
        return workerService.delete(id);
    }

    @GetMapping("/company/workers/salary/max")
    public ResponseEntity<?> getMaxSalary(){
        return workerService.getByMaxSalary();
    }

    @PutMapping("/company/workers/{id}")
    public ResponseEntity<?> updateWorker(@RequestBody Worker w, @PathVariable Integer id){
        return workerService.put(w, id);
    }

    @GetMapping("/company/workers/count")
    public ResponseEntity<?> getAmountByEndDate(@RequestParam(value = "enddate") String endDate,
                                                @RequestParam(value = "condition") String condition){
        return workerService.getAmountByEndDate(endDate,condition);
    }

    @GetMapping("/company/workers")
    public ResponseEntity<?> getWokersBySortAndFilter(@RequestParam(value = "sortElement") List<String> sort,
                                                      @RequestParam(value = "filters") List<String> filters,
                                                      @RequestParam(value = "isUpper") Boolean isUpper,
                                                      @RequestParam(value = "pageSize") Integer pageSize,
                                                      @RequestParam(value = "page") Integer pageNum){
        return workerService.getList(sort,filters,isUpper,pageSize,pageNum);
    }

}
