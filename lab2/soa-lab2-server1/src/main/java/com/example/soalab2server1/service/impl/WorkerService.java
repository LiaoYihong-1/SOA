package com.example.soalab2server1.service.impl;

import com.example.soalab2server1.dao.model.Coordinate;
import com.example.soalab2server1.dao.model.Worker;
import com.example.soalab2server1.dao.repository.WorkerRepository;
import com.example.soalab2server1.service.operation.ServiceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WorkerService implements ServiceOperation<Worker> {
    @Autowired
    WorkerRepository workerRepository;

    @Override
    public ResponseEntity<?> getById(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);

        if (optionalWorker.isPresent()) {
            return ResponseEntity.ok(optionalWorker.get());
        } else {
            Error e = new Error();
            e.setMessage("The specified resource is not found\n");
            e.setCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @Override
    public ResponseEntity<?> getAmountByEndDate(String endDate, String condition) {
        LocalDate d = LocalDate.parse(endDate);
        List<Worker> workers = workerRepository.findAll();

        String regex = "\\d\\d\\d\\d\\-(0[1-9]|1[0-2])\\-(0[1-9]|[1-2][0-9]|3[1-2])"; // 匹配只包含字母的字符串
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(endDate);
        if (!matcher.matches()) {
            return ResponseEntity.status(400).body(new Error("Invalid request", 400));
        }

        int result = 0;
        switch (condition) {
            case "greater":
                for (Worker w : workers) {
                    if (d.compareTo(w.getEndDate()) < 0) {
                        result++;
                    }
                }
                return ResponseEntity.ok(new NumberOfWorkers(result));
            case "equals":
                for (Worker w : workers) {
                    if (d.compareTo(w.getEndDate()) == 0) {
                        result++;
                    }
                }
                return ResponseEntity.ok(new NumberOfWorkers(result));
            default:
                return ResponseEntity.status(400).body(new Error("Invalid request", 400));
        }
    }

    @Override
    public ResponseEntity<?> getByMaxSalary() {
        List<Worker> workers = workerRepository.findAll();
        if (workers.size() == 0) {
            return ResponseEntity.ok("No workers in company\n");
        } else {
            Worker result = workers.get(0);
            for (Worker w : workers) {
                if (w.getSalary() > result.getSalary()) {
                    result = w;
                }
            }
            return ResponseEntity.ok(result);
        }
    }


    @Override
    public ResponseEntity<?> delete(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);

        if (optionalWorker.isPresent()) {
            workerRepository.deleteWorkerById(id);
            return ResponseEntity.ok().body("Deleted");
        } else {
            Error e = new Error();
            e.setMessage("The specified resource is not found\n");
            e.setCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @Override
    public ResponseEntity<?> put(Worker worker, Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);

        if (optionalWorker.isPresent()) {
            worker.setId(id);
            workerRepository.saveAndFlush(worker);
            return ResponseEntity.ok(worker);
        } else {
            Error e = new Error();
            e.setMessage("The specified resource is not found\n");
            e.setCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @Override
    public ResponseEntity<?> post(Worker worker) {
        workerRepository.save(worker);
        return ResponseEntity.ok(worker);
    }

    @Override
    public ResponseEntity<?> getList(List<String> sort, List<String> filters, boolean isUpper, Integer pageSize, Integer pageNum) {
        //sort
        Sort sort1;
        List<Sort.Order> orders = new ArrayList<>();
        Class<Worker> clazz = Worker.class;
        if (isUpper) {
            for (String s : sort) {
                if (!hasProperty(clazz, s)) {
                    return ResponseEntity.status(400).body(new Error("Invalid input", 400));
                }
                orders.add(new Sort.Order(Sort.Direction.ASC, s));
            }
        } else {
            for (String s : sort) {
                if (!hasProperty(clazz, s)) {
                    return ResponseEntity.status(400).body(new Error("Invalid input", 400));
                }
                orders.add(new Sort.Order(Sort.Direction.DESC, s));
            }
        }
        sort1 = Sort.by(orders);
        //filter
        List<Worker> workersAfterFilter = workerRepository.findAll(sort1);
        //page
        Page<Worker> pages = new PageImpl<>(workersAfterFilter, PageRequest.of(pageNum, pageSize), workersAfterFilter.size());
        return ResponseEntity.ok(pages);
    }
    public List<Worker> filter(List<Worker> workers, List<String> filters){
        for(int i = 0 ; i < workers.size(); i++){
            for (String filter : filters) {
                if (filter != null && !filter.isEmpty()) {
                    String conditionRegex = "\\[(.*?)\\]";
                    String valueRegex = "\\](.*?)$";
                    String propertyRegex = "(.*?)\\[";

                    Pattern conditionPattern = Pattern.compile(conditionRegex);
                    Matcher conditionMatcher = conditionPattern.matcher(filter);

                    Pattern valuePattern = Pattern.compile(valueRegex);
                    Matcher valueMatcher = valuePattern.matcher(filter);

                    Pattern propertyPattern = Pattern.compile(propertyRegex);
                    Matcher propertyMatcher = propertyPattern.matcher(filter);

                    if (propertyMatcher.find() && conditionMatcher.find() && valueMatcher.find()) {
                        String property = propertyMatcher.group(1).trim();
                        String value = valueMatcher.group(1).trim();
                        String condition = conditionMatcher.group(1).trim();
                        switch (condition){

                            case "eq":
                                if(property.equals("id"))
                                break;
                            case "gt":
                                break;
                            case "lt":
                                break;
                            default:
                                break;
                        };
                    }
                }
            }
        }
        return workers;
    }
    private boolean hasProperty (Class <?> clazz, String propertyName){
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
