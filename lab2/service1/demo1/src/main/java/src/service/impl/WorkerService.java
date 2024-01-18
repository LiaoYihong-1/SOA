package src.service.impl;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.ejb3.annotation.Pool;
import src.dao.model.*;
import src.dao.model.Enum.Position;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Stateless
@Slf4j
@Pool(value = "i-hate-ejb")
public class WorkerService implements ServiceOperation {
    @PersistenceContext
    private EntityManager entityManager;
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
    public void delete(Integer id) {
        Worker worker = workerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        workerRepository.delete(worker.getId());
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
        log.info(requestWorker.toString());
        Worker worker = mapWorkerInfoToWorker(requestWorker);
        worker.setId(id);

        worker = workerRepository.saveAndFlush(worker);
        log.info("save");
        return mapWorkerToWorkerFullInfo(worker);
    }
    @Override
    public WorkerFullInfo createWorker(CreateWorkerRequest requestWorker) {
        log.info("createWorker");
        log.info(requestWorker.toString());
        if (requestWorker.getOrganization() == null) throw new ResourceNotFoundException("");
        if (!organizationRepository.existsById(requestWorker.getOrganization().getId())
                || !organizationRepository.findById(
                requestWorker.getOrganization().getId()).get().equals(requestWorker.getOrganization())
        )
            throw new ResourceNotFoundException("");

        Worker worker = mapCreateWorkerRequestToWorker(requestWorker);
        worker.setCreationDate(ZonedDateTime.now().withNano(0));
        worker = workerRepository.saveAndFlush(worker);
        log.info(worker.toString());
        return mapWorkerToWorkerFullInfo(worker);
    }

    @Override
    public Page<?> getList(List<String> sortElements, List<String> filters, Boolean isUpper, Integer pageSize, Integer pageNum) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Worker> criteriaQuery = criteriaBuilder.createQuery(Worker.class);
        Root<Worker> root = criteriaQuery.from(Worker.class);

        if (sortElements != null && !sortElements.isEmpty()) {
            applySorting(sortElements, isUpper, criteriaBuilder, criteriaQuery, root);
        }

        if (filters != null && !filters.isEmpty()) {
            applyFilters(filters, criteriaBuilder, criteriaQuery, root);
        }

        List<Worker> resultList = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNum * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        List<WorkerFullInfo> resultListNew = resultList.stream().map(this::mapWorkerToWorkerFullInfo).toList();
        Page<WorkerFullInfo> pages = buildPage(resultListNew, pageSize, pageNum);
        return pages;
    }

    private Page<WorkerFullInfo> buildPage(List<WorkerFullInfo> resultListNew, Integer pageSize, Integer pageNum) {
        return Page.<WorkerFullInfo>builder()
                .content(resultListNew)
                .pagenumber(pageNum)
                .numberOfElements(resultListNew.size())
                .first(pageNum == 0)
                .last(resultListNew.size() < pageSize)
                .hasNext(resultListNew.size() == pageSize)
                .hasPrevious(pageNum > 0)
                .totalPages(1)
                .totalElements(resultListNew.size())
                .hasContent(!resultListNew.isEmpty())
                .build();
    }
    private void applySorting(List<String> sortElements, Boolean isUpper, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<Worker> root) {
        sortElements.forEach(element -> {
            Path<?> path = getPath(root, element);
            if (path != null) {
                criteriaQuery.orderBy(isUpper ? criteriaBuilder.asc(path) : criteriaBuilder.desc(path));
            }
        });
    }
    private void applyFilters(List<String> filters, CriteriaBuilder criteriaBuilder, CriteriaQuery<Worker> criteriaQuery, Root<Worker> root)  {
        List<Predicate> predicates = filters.stream()
                .map(filter -> processFilter(filter, root, criteriaBuilder))
                .filter(Objects::nonNull)
                .toList();

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
    }
    private Predicate processFilter(String filter, Root<Worker> root, CriteriaBuilder criteriaBuilder) {
        String[] parts = filter.split("\\[|\\]");
        String field = parts[0];
        String value = parts[2].split("=")[1];
        String operator = parts[1];
        log.info("field {}, value {}, operator {}", field, value, operator);

        Path<?> path = getPath(root, field);
        if (path == null) {
            return null;
        }

        if (Arrays.asList("name", "organization.fullName", "position").contains(field)) {
            log.info("buildStringPredicate");
            log.info("Path type: {}", path.getJavaType().getName());
            log.info("Value type: {}", value.getClass());
            Predicate p = buildStringPredicate(path, operator, value, criteriaBuilder);
            log.info("success");
            return  p;
        } else {
            log.info("buildPredicate");
            return buildPredicate(path, field, operator, value, criteriaBuilder);
        }
    }
    private Predicate buildStringPredicate(Path<?> path, String operator, String value, CriteriaBuilder criteriaBuilder) {
        return switch (operator) {
            case "eq" -> criteriaBuilder.equal(path, value);
            case "ne" -> criteriaBuilder.notEqual(path, value);
            case "gt" -> {
                yield criteriaBuilder.greaterThan((Expression<String>) path, value);
            }
            case "lt" -> {
                yield criteriaBuilder.lessThan((Expression<String>) path, value);
            }
            default -> null;
        };
    }
    private Predicate buildPredicate(Path<?> path, String field, String operator, String value, CriteriaBuilder criteriaBuilder) {
        Object parsedValue = parseValue(value, field);
        return switch (operator) {
            case "eq" -> criteriaBuilder.equal(path, parsedValue);
            case "ne" -> criteriaBuilder.notEqual(path, parsedValue);
            case "gt" -> criteriaBuilder.greaterThan((Expression<? extends Comparable>) path, (Comparable) parsedValue);
            case "lt" -> criteriaBuilder.lessThan((Expression<? extends Comparable>) path, (Comparable) parsedValue);
            case "lte" ->
                    criteriaBuilder.lessThanOrEqualTo((Expression<? extends Comparable>) path, (Comparable) parsedValue);
            case "gte" ->
                    criteriaBuilder.greaterThanOrEqualTo((Expression<? extends Comparable>) path, (Comparable) parsedValue);
            default -> null;
        };
    }
    private Object parseValue(String value, String field) {
        return switch (field) {
            case "creationdate" -> ZonedDateTime.parse(value).minusHours(3);
            case "startdate" -> LocalDate.parse(value).atStartOfDay();
            case "enddate" -> LocalDate.parse(value);
            case "id", "organization.id" -> Integer.valueOf(value);
            case "organization.annualTurnover", "coordinates.x" -> Long.valueOf(value);
            case "salary", "coordinates.y" -> Float.valueOf(value);
            default -> value;
        };
    }
    private Path<?> getPath(Root<Worker> root, String field) {
        // todo Worker_.field and etc...(but its like not needed in lab)
        return switch (field) {
            case "id" -> root.get("id");
            case "name" -> root.get("name");
            case "creationdate" -> root.get("creationDate");
            case "salary" -> root.get("salary");
            case "startdate" -> root.get("startDate");
            case "enddate" -> root.get("endDate");
            case "position" -> root.get("position");
            case "coordinates.x" -> root.get("coordinate").get("x");
            case "coordinates.y" -> root.get("coordinate").get("y");
            case "organization.fullName" -> {
                Join<Worker, Organization> organizationJoin = root.join("organization");
                yield organizationJoin.get("fullName");
            }
            case "organization.annualTurnover" -> {
                Join<Worker, Organization> organizationJoin = root.join("organization");
                yield organizationJoin.get("annualTurnover");
            }
            case "organization.id" -> {
                Join<Worker, Organization> organizationJoin = root.join("organization");
                yield organizationJoin.get("id");
            }
            default -> null;
        };
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
    private Worker mapWorkerInfoToWorker(WorkerInfo workerInfo) {
        log.info("mapWorkerInfoToWorker");
        Worker worker = new Worker();
        worker.setName(workerInfo.getName());
        worker.setCoordinate(workerInfo.getCoordinate());
        log.info("1");
        worker.setCreationDate(workerInfo.getCreationDate());
        log.info("2");
        worker.setSalary(workerInfo.getSalary());
        worker.setStartDate(workerInfo.getStartDate().atStartOfDay());
        worker.setEndDate(workerInfo.getEndDate());
        log.info("3");
        worker.setPosition(Position.valueOf(workerInfo.getPosition().toUpperCase()));
        if (workerInfo.getOrganization() != null)
            worker.setOrganization(workerInfo.getOrganization());
        log.info("4");
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
        System.out.println("mapWorkerToWorkerInfo " + worker.getPosition());
        workerFullInfo.setPosition(worker.getPosition());
        workerFullInfo.setOrganization(worker.getOrganization());
        return workerFullInfo;
    }
}
