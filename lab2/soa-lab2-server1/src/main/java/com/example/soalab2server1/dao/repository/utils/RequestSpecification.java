package com.example.soalab2server1.dao.repository.utils;

import com.example.soalab2server1.dao.model.Enum.Position;
import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.model.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
@Slf4j
public class RequestSpecification implements Specification<Worker> {

    private final List<String> filter;
    public Predicate toPredicate(Root<Worker> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = Optional.ofNullable(filter)
                .map(f -> f.stream()
                        .map(filter -> processFilter(filter, root, criteriaBuilder))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        log.info("criteriaBuilder");
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
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

    private Predicate buildStringPredicate(
            Path<?> path,
            String operator,
            String value,
            CriteriaBuilder criteriaBuilder
    ) {
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


    private Predicate buildPredicate(
            Path<?> path,
            String field,
            String operator,
            String value,
            CriteriaBuilder criteriaBuilder
    ) {
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
}