package com.example.soalab2server1.dao.repository.utils;

import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.model.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(staticName = "of") @Slf4j
public class RequestSpecification implements Specification<Worker> {

    private final List<String> filter;

    @Override
    public Predicate toPredicate(Root<Worker> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        filter.forEach(it -> {
            String[] parts = it.split("\\[|\\]");
            String field = parts[0];
            String value = parts[2].split("=")[1];
            String operator = parts[1];

            Path<?> path = getPath(root, field);

            if (path != null) {
                Predicate predicate = buildPredicate(path, operator, value, criteriaBuilder);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        });
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate buildPredicate(Path<?> path, String operator, String value, CriteriaBuilder criteriaBuilder) {
        return switch (operator) {
            case "eq" -> criteriaBuilder.equal(path.as(String.class), value);
            case "ne" -> criteriaBuilder.notEqual(path.as(String.class), value);
            case "gt" -> criteriaBuilder.greaterThan(path.as(String.class), value);
            case "lt" -> criteriaBuilder.lessThan(path.as(String.class), value);
            case "lte" -> criteriaBuilder.lessThanOrEqualTo(path.as(String.class), value);
            case "gte" -> criteriaBuilder.greaterThanOrEqualTo(path.as(String.class), value);
            default -> null;
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