package src.service.impl;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import src.dao.model.Enum.Position;
import src.dao.model.NumberOfWorkers;
import src.dao.model.Page;
import src.dao.model.Worker;
import src.dao.model.WorkerFullInfo;
import src.dao.repository.OrganizationRepI;
import src.dao.repository.WorkerRepI;
import src.dao.request.CreateWorkerRequest;
import src.dao.request.WorkerInfo;
import src.excep.InvalidConditionException;
import src.excep.ResourceNotFoundException;
import src.service.operation.ServiceOperation;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import static jakarta.ws.rs.core.Response.*;


@Stateless
@Slf4j
public class WorkerService implements ServiceOperation {
    @Inject
    private OrganizationRepI organizationRepository;
    @Inject
    private WorkerRepI workerRepository;

    @Override
    public WorkerFullInfo getById(Integer id) {
        Worker optionalWorker = workerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        return mapWorkerToWorkerFullInfo(optionalWorker);
    }

    @Override
    public String hello() {
        return "null";
    }

    @Override
    public NumberOfWorkers getAmountByEndDate(String endDate, String condition) throws InvalidParameterException {
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
        return new NumberOfWorkers(workerRepository.countWorkersByEndDateAndCondition(d, condition));
    }

    @Override
    public WorkerFullInfo getByMaxSalary() {
        Worker worker = workerRepository.findWorkerByMaxSalary().orElseThrow(() -> new ResourceNotFoundException(""));
        return mapWorkerToWorkerFullInfo(worker);
    }

    @Override
    public Response delete(Integer id) {
        Worker worker = workerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        workerRepository.delete(worker.getId());
        return Response.status(Status.NO_CONTENT).build();
    }

    @Override
    public  WorkerFullInfo updateWorker(WorkerInfo requestWorker, Integer id) {
        log.info("updateWorker");
        if (!workerRepository.existsById(id)) throw new ResourceNotFoundException("");
        if (requestWorker.getOrganization() != null)
            if (requestWorker.getOrganization().getId() != null)
                if (!organizationRepository.existsById(requestWorker.getOrganization().getId())
                        || !organizationRepository.findById(
                        requestWorker.getOrganization().getId()).get().equals(requestWorker.getOrganization())
                )
                    throw new ResourceNotFoundException("");
        log.info("start");
        Worker worker = mapWorkerInfoToWorker(requestWorker);
        worker.setId(id);
        worker = workerRepository.saveAndFlush(worker);
        log.info("save");
        return mapWorkerToWorkerFullInfo(worker);
    }
    public static Worker mapWorkerInfoToWorker(WorkerInfo workerInfo) {
        Worker worker = new Worker();
        worker.setName(workerInfo.getName());
        worker.setCoordinate(workerInfo.getCoordinate());
        worker.setCreationDate(ZonedDateTime.from(workerInfo.getStartDate().atStartOfDay()));
        worker.setSalary(workerInfo.getSalary());
        worker.setStartDate(workerInfo.getStartDate().atStartOfDay());
        worker.setEndDate(workerInfo.getEndDate());
        worker.setPosition(Position.valueOf(workerInfo.getPosition().toUpperCase()));
        worker.setOrganization(workerInfo.getOrganization());
        return worker;
    }
    @Override
    public WorkerFullInfo createWorker(CreateWorkerRequest requestWorker) {
        if (requestWorker.getOrganization() == null) throw new ResourceNotFoundException("");
        if (!organizationRepository.existsById(requestWorker.getOrganization().getId())
                || !organizationRepository.findById(
                requestWorker.getOrganization().getId()).get().equals(requestWorker.getOrganization())
        )
            throw new ResourceNotFoundException("");
        log.info("createWorker");
        Worker worker = mapCreateWorkerRequestToWorker(requestWorker);
        worker.setCreationDate(ZonedDateTime.now().withNano(0));
        worker = workerRepository.saveAndFlush(worker);
        log.info(worker.toString());
        return mapWorkerToWorkerFullInfo(worker);
    }

    @Override
    public Page<?> getList(List<String> sortElements, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum) {

//        List<Sort.Order> orders;
//        PageRequest pageable = PageRequest.of(pageNum, pageSize);
//        Page<WorkerFullInfo> entities;
//
//        if (sortElements != null) {
//
//            if (sortElements.isEmpty() || isUpper == null) {
//                log.info("sortElements.isEmpty() || isUpper == null");
//                throw new InvalidParameterException("");
//            }
//
//            if (sortElements.size() != sortElements.stream().distinct().count()) {
//                log.info("sortElements.size() != sortElements.stream().distinct().count()");
//                throw new InvalidParameterException("");
//            }
//
//            sortElements.stream().filter(it -> !it.equals("coordinates.x") && !it.equals("coordinates.y") && !it.equals("organization.id") && !it.equals("organization.fullName") && !it.equals("organization.annualTurnover")).forEach(it -> {
//                if (!isSortableField(Worker.class, it)) {
//                    log.info("!isSortableField(Worker.class, it)");
//                    throw new InvalidParameterException(it);
//                }
//            });
//
//            sortElements.replaceAll(it -> it.equals("coordinates.x") ? "coordinate.x" : it);
//            sortElements.replaceAll(it -> it.equals("coordinates.y") ? "coordinate.y" : it);
//
//
//            orders = sortElements.stream().map(element -> isUpper ? new Sort.Order(Sort.Direction.ASC, element) : new Sort.Order(Sort.Direction.DESC, element)).toList();
//
//            pageable = PageRequest.of(pageNum, pageSize, Sort.by(orders));
//
//        }
//        if (filters != null) {
//            if (filters.size() != filters.stream().distinct().count() || filters.isEmpty()) {
//                log.info("filters.size() != filters.stream().distinct().count() || filters.isEmpty()");
//                throw new InvalidParameterException("");
//            }
//            filters.forEach(it -> {
//                try {
//                    String[] parts = it.split("\\[|\\]");
//                    String field = parts[0];
//                    String value = parts[2].split("=")[1];
//                    String operator = parts[1];
//
//                    if (!operator.matches("eq|ne|gt|lt|lte|gte")) throw new InvalidParameterException("");
//                    if ((field.equals("name") || field.equals("position") || field.equals("organization.fullName")) && (!operator.matches("eq|ne|gt|lt")))
//                        throw new InvalidParameterException("");
//
//                    SimpleDateFormat creationdateDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//                    SimpleDateFormat startdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//                    if (field.equals("creationdate")) {
//                        Date date = creationdateDateFormat.parse(value);
//                    }
//                    if (field.equals("startdate") || field.equals("enddate")) {
//                        Date date = startdateDateFormat.parse(value);
//                    }
//                    if (field.equals("id") || field.equals("organization.id")) Integer.parseInt(value);
//
//                    if (field.equals("organization.annualTurnover") || field.equals("coordinates.x"))
//                        Long.parseLong(value);
//
//                    if (field.equals("salary")) Float.parseFloat(value);
//
//                    if (field.equals("coordinates.y")) Double.parseDouble(value);
//
//                } catch (Exception ex) {
//                    throw new InvalidParameterException("");
//                }
//            });
//        }
        log.info("before findAll");
//        entities = workerRepository.findAll(RequestSpecification.of(filters), pageable).map(it -> modelMapper.map(it, WorkerFullInfo.class));
//        return Page.of(entities);
        System.out.println("я забилдился до этого момента!");
        return null;
    }

    private boolean isSortableField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private Worker mapCreateWorkerRequestToWorker(CreateWorkerRequest createWorkerRequest) {
        Worker worker = new Worker();
        worker.setName(createWorkerRequest.getName());
        worker.setCoordinate(createWorkerRequest.getCoordinate());
        worker.setSalary(createWorkerRequest.getSalary());
        worker.setStartDate(createWorkerRequest.getStartDate().atStartOfDay());
        worker.setEndDate(createWorkerRequest.getEndDate());
        worker.setPosition(Position.valueOf(createWorkerRequest.getPosition().toUpperCase()));
        worker.setOrganization(createWorkerRequest.getOrganization());
        return worker;
    }

    private WorkerInfo mapWorkerToWorkerInfo(Worker worker) {
        WorkerInfo workerInfo = new WorkerInfo();
        workerInfo.setName(worker.getName());
        workerInfo.setCoordinate(worker.getCoordinate());
        workerInfo.setCreationDate(worker.getCreationDate());
        workerInfo.setSalary((float) worker.getSalary());
        workerInfo.setStartDate(worker.getStartDate().toLocalDate());
        workerInfo.setEndDate(worker.getEndDate());
        workerInfo.setPosition(worker.getPosition());
        workerInfo.setOrganization(worker.getOrganization());
        return workerInfo;
    }

    private WorkerFullInfo mapWorkerToWorkerFullInfo(Worker worker) {
        WorkerFullInfo workerFullInfo = new WorkerFullInfo();
        workerFullInfo.setId(worker.getId());
        workerFullInfo.setName(worker.getName());
        workerFullInfo.setCoordinate(worker.getCoordinate());
        workerFullInfo.setCreationDate(worker.getCreationDate().toLocalDateTime());
        workerFullInfo.setSalary((float) worker.getSalary());
        workerFullInfo.setStartDate(worker.getStartDate().toLocalDate());
        workerFullInfo.setEndDate(worker.getEndDate());
        workerFullInfo.setPosition(worker.getPosition());
        workerFullInfo.setOrganization(worker.getOrganization());
        return workerFullInfo;
    }
}
