package com.example.soalab2server1.dao.repository.utils;

import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.model.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Slf4j
public class RequestSpecification implements Specification<Worker> {

    private final List<String> filter;

    @Override
    public Predicate toPredicate(Root<Worker> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter != null)
            filter.forEach(it -> {
                String[] parts = it.split("\\[|\\]");
                String field = parts[0];
                String value = parts[2].split("=")[1];
                String operator = parts[1];

                Path<?> path = getPath(root, field);

                if (path != null) {
                    Predicate predicate = buildPredicate(path, field, operator, value, criteriaBuilder);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                }
            });
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate buildPredicate(
            Path<?> path,
            String field,
            String operator,
            String value,
            CriteriaBuilder criteriaBuilder
    ) {
        Object parsedValue = parseValue(value, field);
        //todo check db
        return switch (operator) {
            case "eq" -> criteriaBuilder.equal((Expression<? extends Comparable>) path, (Comparable) parsedValue);
            case "ne" -> criteriaBuilder.notEqual((Expression<? extends Comparable>)  path, (Comparable) parsedValue);
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
            case "creationdate" -> ZonedDateTime.parse(value);
            case "startdate" -> LocalDate.parse(value).atStartOfDay();
            case "enddate" -> LocalDate.parse(value);
            case "id", "organization.id" -> Integer.valueOf(value);
            case "organization.annualTurnover", "coordinates.x" -> Long.valueOf(value);
            case "salary", "coordinates.y" -> Float.valueOf(value);
            default -> value;// null;
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
            case "organization.name" -> {
                Join<Worker, Organization> organizationJoin = root.join("organization");
                yield organizationJoin.get("full_name");
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