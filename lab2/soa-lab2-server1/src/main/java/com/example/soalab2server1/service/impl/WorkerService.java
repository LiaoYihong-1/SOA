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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


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
        return ResponseEntity.ok(modelMapper.map(optionalWorker, WorkerFullInfo.class));
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
        return ResponseEntity.ok(modelMapper.map(worker, WorkerFullInfo.class));
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Worker worker = workerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        workerRepository.delete(worker);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The worker was deleted successfully");
    }

    @Override
    public ResponseEntity<?> updateWorker(WorkerInfo requestWorker, Integer id) {
        if (!workerRepository.existsById(id)) throw new ResourceNotFoundException("");
        if (requestWorker.getOrganization() != null)
            if (requestWorker.getOrganization().getId() != null)
                if (!organizationRepository.existsById(requestWorker.getOrganization().getId())
                        || !organizationRepository.findById(
                        requestWorker.getOrganization().getId()).get().equals(requestWorker.getOrganization())
                )
                    throw new ResourceNotFoundException("");
        Worker worker = modelMapper.map(requestWorker, Worker.class);
        worker.setId(id);
        worker = workerRepository.saveAndFlush(worker);
        return ResponseEntity.ok(modelMapper.map(worker, WorkerFullInfo.class));
    }

    @Override
    public ResponseEntity<?> createWorker(CreateWorkerRequest requestWorker) {
        if (requestWorker.getOrganization() == null) throw new ResourceNotFoundException("");
        if (!organizationRepository.existsById(requestWorker.getOrganization().getId())
                || !organizationRepository.findById(
                requestWorker.getOrganization().getId()).get().equals(requestWorker.getOrganization())
        )
            throw new ResourceNotFoundException("");
        log.info("createWorker");
        Worker worker = modelMapper.map(requestWorker, Worker.class);
        worker.setCreationDate(ZonedDateTime.now().withNano(0));
        worker = workerRepository.saveAndFlush(worker);
        log.info(worker.toString());
        return ResponseEntity.ok(modelMapper.map(worker, WorkerFullInfo.class));
    }

    @Override
    public com.example.soalab2server1.dao.model.Page<?> getList(List<String> sortElements, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum) {

        List<Sort.Order> orders;
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Page<WorkerFullInfo> entities;

        if (sortElements != null) {

            if (sortElements.isEmpty() || isUpper == null) {
                log.info("sortElements.isEmpty() || isUpper == null");
                throw new InvalidParameterException("");
            }

            if (sortElements.size() != sortElements.stream().distinct().count()) {
                log.info("sortElements.size() != sortElements.stream().distinct().count()");
                throw new InvalidParameterException("");
            }

            sortElements.stream().filter(it -> !it.equals("coordinates.x") && !it.equals("coordinates.y") && !it.equals("organization.id") && !it.equals("organization.fullName") && !it.equals("organization.annualTurnover")).forEach(it -> {
                if (!isSortableField(Worker.class, it)) {
                    log.info("!isSortableField(Worker.class, it)");
                    throw new InvalidParameterException(it);
                }
            });

            sortElements.replaceAll(it -> it.equals("coordinates.x") ? "coordinate.x" : it);
            sortElements.replaceAll(it -> it.equals("coordinates.y") ? "coordinate.y" : it);


            orders = sortElements.stream().map(element -> isUpper ? new Sort.Order(Sort.Direction.ASC, element) : new Sort.Order(Sort.Direction.DESC, element)).toList();

            pageable = PageRequest.of(pageNum, pageSize, Sort.by(orders));

        }
        if (filters != null) {
            if (filters.size() != filters.stream().distinct().count() || filters.isEmpty()) {
                log.info("filters.size() != filters.stream().distinct().count() || filters.isEmpty()");
                throw new InvalidParameterException("");
            }
            filters.forEach(it -> {
                try {
                    String[] parts = it.split("\\[|\\]");
                    String field = parts[0];
                    String value = parts[2].split("=")[1];
                    String operator = parts[1];

                    if (!operator.matches("eq|ne|gt|lt|lte|gte")) throw new InvalidParameterException("");
                    if ((field.equals("name") || field.equals("position") || field.equals("organization.fullName")) && (!operator.matches("eq|ne|gt|lt")))
                        throw new InvalidParameterException("");

                    SimpleDateFormat creationdateDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    SimpleDateFormat startdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    if (field.equals("creationdate")) {
                        Date date = creationdateDateFormat.parse(value);
                    }
                    if (field.equals("startdate") || field.equals("enddate")) {
                        Date date = startdateDateFormat.parse(value);
                    }
                    if (field.equals("id") || field.equals("organization.id")) Integer.parseInt(value);

                    if (field.equals("organization.annualTurnover") || field.equals("coordinates.x"))
                        Long.parseLong(value);

                    if (field.equals("salary")) Float.parseFloat(value);

                    if (field.equals("coordinates.y")) Double.parseDouble(value);

                } catch (Exception ex) {
                    throw new InvalidParameterException("");
                }
            });
        }
        log.info("before findAll");
        entities = workerRepository.findAll(RequestSpecification.of(filters), pageable).map(it -> modelMapper.map(it, WorkerFullInfo.class));
        return com.example.soalab2server1.dao.model.Page.of(entities);
    }

    private boolean isSortableField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
