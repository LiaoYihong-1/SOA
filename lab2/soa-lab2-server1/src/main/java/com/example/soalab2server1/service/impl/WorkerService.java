package com.example.soalab2server1.service.impl;

import com.example.soalab2server1.dao.model.*;
import com.example.soalab2server1.dao.model.Error;
import com.example.soalab2server1.dao.repository.OrganizationRepository;
import com.example.soalab2server1.dao.repository.WorkerRepository;
import com.example.soalab2server1.dao.repository.utils.RequestSpecification;
import com.example.soalab2server1.dao.request.CreateWorkerRequest;
import com.example.soalab2server1.dao.request.WorkerInfo;
import com.example.soalab2server1.exception.InvalidConditionException;
import com.example.soalab2server1.exception.InvalidParameterException;
import com.example.soalab2server1.exception.ResourceNotFoundException;
import com.example.soalab2server1.service.operation.ServiceOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerService implements ServiceOperation<Worker> {
    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;
    private final WorkerRepository workerRepository;

    @Override
    public ResponseEntity<?> getById(Integer id) {
        Worker optionalWorker = workerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        return ResponseEntity.ok(optionalWorker);
    }

    @Override
    public ResponseEntity<?> getAmountByEndDate(String endDate, String condition) throws InvalidParameterException {
        LocalDate d;
        try {
            if (!"greater".equals(condition) && !"equals".equals(condition)) {
                throw new InvalidConditionException("");
            }
            d = LocalDate.parse(endDate);
            log.info(d.toString());
        } catch (InvalidConditionException e) {
            throw new InvalidConditionException("");
        } catch (Exception e) {
            throw new InvalidParameterException("");
        }
        return ResponseEntity.ok(new NumberOfWorkers(workerRepository.countWorkersByEndDateAndCondition(d, condition)));
    }

    @Override
    public ResponseEntity<?> getByMaxSalary() {
        Worker worker = workerRepository.findWorkerByMaxSalary().orElseThrow(() -> new ResourceNotFoundException(""));
        return ResponseEntity.ok(worker);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Worker worker = workerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        workerRepository.delete(worker);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The worker was deleted successfully");
    }

    @Override
    public ResponseEntity<?> updateWorker(WorkerInfo requestWorker, Integer id) {
        if (!workerRepository.existsById(id))
                throw new ResourceNotFoundException("");
        //todo
        if (!organizationRepository.existsById(requestWorker.getOrganization().getId()))
                throw new ResourceNotFoundException("");
        Worker worker = modelMapper.map(requestWorker,Worker.class);
        worker.setId(id);
        worker = workerRepository.saveAndFlush(worker);
        return ResponseEntity.ok(worker);
    }

    @Override
    public ResponseEntity<?> createWorker(CreateWorkerRequest requestWorker) {
        //todo
        if (!organizationRepository.existsById(requestWorker.getOrganization().getId()))
            throw new ResourceNotFoundException("");
        Worker worker = modelMapper.map(requestWorker,Worker.class);
        worker.setCreationDate(ZonedDateTime.now());
        log.info("save");
        worker = workerRepository.saveAndFlush(worker);
        return ResponseEntity.ok(worker);
    }

    @Override
    public com.example.soalab2server1.dao.model.Page<?> getList(List<String> sortElements, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum) {
        //[id[eq]=1, coordinates.x[gte]=2]
        // id
        // eq
        // 1
        Pattern nestedFieldNamePattern = Pattern.compile("(.*)\\.(.*)");
        Pattern lhsPattern = Pattern.compile("(.*)\\[(.*)\\]=(.*)");

        if (sortElements != null && !sortElements.isEmpty()) {
            Boolean finalIsUpper = isUpper;
            List<Sort.Order> orders = sortElements.stream()
                    .map(element -> {
                        boolean isAscending = finalIsUpper != null && finalIsUpper;
                        return isAscending ?
                                new Sort.Order(Sort.Direction.ASC, element)
                                : new Sort.Order(Sort.Direction.DESC, element);
                    })
                    .toList();
            PageRequest pageable = PageRequest.of(pageNum, pageSize,
                    Sort.by(orders));
            val entities = workerRepository.findAll(RequestSpecification.of(filters), pageable);

            return com.example.soalab2server1.dao.model.Page.of(entities);
        }
        return null;
        //sort
//        Sort sort1;
//        List<Sort.Order> orders = new ArrayList<>();
//        Class<Worker> clazz = Worker.class;
//        isUpper = true;
//        if (isUpper) {
//            for (String s : sort) {
//                if (!isSortableField(clazz, s)) {
//                    return ResponseEntity.status(400).body(new Error("Invalid request", 400));
//                }
//                orders.add(new Sort.Order(Sort.Direction.ASC, s));
//            }
//        } else {
//            for (String s : sort) {
//                if (!isSortableField(clazz, s)) {
//                    return ResponseEntity.status(400).body(new Error("Invalid request", 400));
//                }
//                orders.add(new Sort.Order(Sort.Direction.DESC, s));
//            }
//        }
//        sort1 = Sort.by(orders);
//        //page
//        Pageable pageable = PageRequest.of(pageNum, pageSize, sort1);
//        //filter
//        Specification<Worker> spec;
//        try {
//            spec = filterCreate(filters);
//        } catch (InvalidParameterException e) {
//            return ResponseEntity.status(400).body(new Error("Invalid input", 400));
//        }
//        Page<Worker> pages = workerRepository.findAll(spec, pageable);
//        return ResponseEntity.ok(com.example.soalab2server1.dao.model.Page.of(pages));
    }

//    public Specification<Worker> filterCreate(List<String> filters) throws InvalidParameterException {
//        Specification<Worker> spec = (root, query, cb) -> {
//            Join<Worker, Coordinate> coordinateJoin = root.join("coordinate", JoinType.INNER);
//            Predicate predicate = cb.conjunction();
//            for (String filter : filters) {
//                if (filter != null && !filter.isEmpty()) {
//                    String conditionRegex = "\\((.*?)\\)";
//                    String valueRegex = "\\)(.*?)$";
//                    String propertyRegex = "(.*?)\\(";
//                    Pattern conditionPattern = Pattern.compile(conditionRegex);
//                    Matcher conditionMatcher = conditionPattern.matcher(filter);
//
//                    Pattern valuePattern = Pattern.compile(valueRegex);
//                    Matcher valueMatcher = valuePattern.matcher(filter);
//
//                    Pattern propertyPattern = Pattern.compile(propertyRegex);
//                    Matcher propertyMatcher = propertyPattern.matcher(filter);
//
//                    if (conditionMatcher.find() && valueMatcher.find() && propertyMatcher.find()) {
//                        String property = propertyMatcher.group(1);
//                        String value = valueMatcher.group(1);
//                        System.out.println(property);
//                        if ("coordinate.x".equals(property)) {
//                            property = "x";
//                        }
//                        if ("coordinate.y".equals(property)) {
//                            property = "y";
//                        }
//                        switch (conditionMatcher.group(1)) {
//                            case "eq" -> {
//                                /*
//                                 * coordinateJoin only for @Embedded(here is like coordinate.x) object
//                                 * root only for not @Embedded object
//                                 */
//                                if ("id".equals(property)) {
//                                    predicate = cb.and(predicate, cb.equal(root.get(property), Integer.valueOf(value)));
//                                } else if ("x".equals(property)) {
//                                    predicate = cb.and(predicate, cb.equal(coordinateJoin.get(property), Long.valueOf(value)));
//                                } else if ("y".equals(property)) {
//                                    predicate = cb.and(predicate, cb.equal(coordinateJoin.get(property), Double.valueOf(value)));
//                                } else if ("salary".equals(property)) {
//                                    predicate = cb.and(predicate, cb.equal(root.get(property), Float.valueOf(value)));
//                                } else {
//                                    predicate = cb.and(predicate, cb.equal(root.get(property), value));
//                                }
//                            }
//                            case "gt" -> {
//                                if ("id".equals(property)) {
//                                    predicate = cb.and(predicate, cb.greaterThan(root.get(property), Integer.valueOf(value)));
//                                } else if ("x".equals(property)) {
//                                    predicate = cb.and(predicate, cb.greaterThan(coordinateJoin.get(property), Long.valueOf(value)));
//                                } else if ("y".equals(property)) {
//                                    predicate = cb.and(predicate, cb.greaterThan(coordinateJoin.get(property), Double.valueOf(value)));
//                                } else if ("salary".equals(property)) {
//                                    predicate = cb.and(predicate, cb.greaterThan(root.get(property), Float.valueOf(value)));
//                                } else {
//                                    predicate = cb.and(predicate, cb.greaterThan(root.get(property), value));
//                                }
//                            }
//                            case "lt" -> {
//                                if ("id".equals(property)) {
//                                    predicate = cb.and(predicate, cb.lessThan(root.get(property), Integer.valueOf(value)));
//                                }
//                                if ("x".equals(property)) {
//                                    predicate = cb.and(predicate, cb.lessThan(coordinateJoin.get(property), Long.valueOf(value)));
//                                }
//                                if ("y".equals(property)) {
//                                    predicate = cb.and(predicate, cb.lessThan(coordinateJoin.get(property), Double.valueOf(value)));
//                                }
//                                if ("salary".equals(property)) {
//                                    predicate = cb.and(predicate, cb.lessThan(root.get(property), Float.valueOf(value)));
//                                } else {
//                                    predicate = cb.and(predicate, cb.lessThan(root.get(property), value));
//                                }
//                            }
//                            default -> throw new InvalidParameterException("Condition not in consideration\n");
//                        }
//
//                    }
//                }
//            }
//            return predicate;
//        };
//        return spec;
//    }
    private boolean isSortableField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
