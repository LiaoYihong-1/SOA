package com.example.soalab2server1.service.impl;

import com.example.soalab2server1.dao.model.*;
import com.example.soalab2server1.dao.model.Error;
import com.example.soalab2server1.dao.repository.WorkerRepository;
import com.example.soalab2server1.service.operation.ServiceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
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
            e.setMessage("The specified resource is not found");
            e.setCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @Override
    public ResponseEntity<?> getAmountByEndDate(String endDate, String condition) {
        LocalDate d = LocalDate.parse(endDate);
        List<Worker> workers = workerRepository.findAll();

        String regex = "\\d\\d\\d\\d\\-(0[1-9]|1[0-2])\\-(0[1-9]|[1-2][0-9]|3[1-2])"; //only number
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
            return ResponseEntity.ok("No workers in company");
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
            workerRepository.delete(optionalWorker.get());
            Error e = new Error();
            e.setMessage("The worker was fired successfully");
            e.setCode(200);
            return ResponseEntity.ok().body(e);
        } else {
            Error e = new Error();
            e.setMessage("The specified resource is not found");
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
            e.setMessage("The specified resource is not found");
            e.setCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @Override
    @Transactional
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
        //page
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort1);
        //filter
        Specification<Worker> spec;
        try {
            spec = filterCreate(filters);
        }catch (InvalidParameterException e){
            return ResponseEntity.status(400).body(new Error("Invalid input", 400));
        }
        Page<Worker> pages = workerRepository.findAll(spec,pageable);
        MyPage<Worker> result = new MyPage<>();
        result.buildWithPage(pages);
        return ResponseEntity.ok(result);
    }

    public Specification<Worker> filterCreate(List<String> filters) throws InvalidParameterException{
        Specification<Worker> spec = (root, query, cb) -> {
            Join<Worker, Coordinate> coordinateJoin = root.join("coordinate", JoinType.INNER);
            Predicate predicate = cb.conjunction();
            for(String filter: filters) {
                if(filter != null && !filter.isEmpty()) {
                    String conditionRegex = "\\((.*?)\\)";
                    String valueRegex = "\\)(.*?)$";
                    String propertyRegex = "(.*?)\\(";
                    Pattern conditionPattern = Pattern.compile(conditionRegex);
                    Matcher conditionMatcher = conditionPattern.matcher(filter);

                    Pattern valuePattern = Pattern.compile(valueRegex);
                    Matcher valueMatcher = valuePattern.matcher(filter);

                    Pattern propertyPattern = Pattern.compile(propertyRegex);
                    Matcher propertyMatcher = propertyPattern.matcher(filter);

                    if(conditionMatcher.find() && valueMatcher.find() && propertyMatcher.find()){
                        String property = propertyMatcher.group(1);
                        String value = valueMatcher.group(1);
                        System.out.println(property);
                        if("coordinate.x".equals(property)){
                            property = "x";
                        }
                        if("coordinate.y".equals(property)){
                            property = "y";
                        }
                        switch(conditionMatcher.group(1)){
                            case "eq":
                                /**
                                 * coordinateJoin only for @Embedded(here is like coordinate.x) object
                                 * root only for not @Embedded object
                                 */
                                if("id".equals(property)) {
                                    predicate = cb.and(predicate, cb.equal(root.get(property), Integer.valueOf(value)));
                                }else if("x".equals(property)){
                                    predicate = cb.and(predicate, cb.equal(coordinateJoin.get(property), Long.valueOf(value)));
                                }else if("y".equals(property)){
                                    predicate = cb.and(predicate, cb.equal(coordinateJoin.get(property), Double.valueOf(value)));
                                }else if("salary".equals(property)){
                                    predicate = cb.and(predicate, cb.equal(root.get(property), Float.valueOf(value)));
                                }else{
                                    predicate = cb.and(predicate, cb.equal(root.get(property), value));
                                }
                                break;
                            case "gt":
                                if("id".equals(property)) {
                                    predicate = cb.and(predicate, cb.greaterThan(root.get(property), Integer.valueOf(value)));
                                }else if("x".equals(property)){
                                    predicate = cb.and(predicate, cb.greaterThan(coordinateJoin.get(property), Long.valueOf(value)));
                                }else if("y".equals(property)){
                                    predicate = cb.and(predicate, cb.greaterThan(coordinateJoin.get(property), Double.valueOf(value)));
                                }else if("salary".equals(property)){
                                    predicate = cb.and(predicate, cb.greaterThan(root.get(property), Float.valueOf(value)));
                                }else{
                                    predicate = cb.and(predicate, cb.greaterThan(root.get(property), value));
                                }
                                break;
                            case "lt":
                                if("id".equals(property)) {
                                    predicate = cb.and(predicate, cb.lessThan(root.get(property), Integer.valueOf(value)));
                                }else if("x".equals(property)){
                                    predicate = cb.and(predicate, cb.lessThan(coordinateJoin.get(property), Long.valueOf(value)));
                                }else if("y".equals(property)){
                                    predicate = cb.and(predicate, cb.lessThan(coordinateJoin.get(property), Double.valueOf(value)));
                                }else if("salary".equals(property)){
                                    predicate = cb.and(predicate, cb.lessThan(root.get(property), Float.valueOf(value)));
                                }else{
                                    predicate = cb.and(predicate, cb.lessThan(root.get(property), value));
                                }
                                break;
                            default:
                                throw new InvalidParameterException("Condition not in consideration\n");
                        };
                    }
                }
            }
            return predicate;
        };
        return spec;
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
