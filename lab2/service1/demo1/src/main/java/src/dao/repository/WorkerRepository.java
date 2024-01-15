package src.dao.repository;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import src.dao.model.Worker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WorkerRepository implements WorkerRepI {

    @PersistenceContext
    private EntityManager entityManager;
    public Worker saveAndFlush(Worker worker) {
        entityManager.persist(worker);
        entityManager.flush();

        return worker;
    }
    public void delete(Integer id) {
        Worker worker = entityManager.find(Worker.class, id);
        if (worker != null) {
            entityManager.remove(worker);
        }
    }
    public boolean existsById(Integer id) {
        Worker worker = entityManager.find(Worker.class, id);
        return worker != null;
    }
//    public List<Worker> findAll(RequestSpecification specification, Pageable pageable) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Worker> criteriaQuery = criteriaBuilder.createQuery(Worker.class);
//        Root<Worker> root = criteriaQuery.from(Worker.class);
//
//        criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
//
//        if (pageable.getSort() != null) {
//            criteriaQuery.orderBy(
//                    pageable.getSort().stream()
//                            .map(order -> order.isAscending() ?
//                                    criteriaBuilder.asc(root.get(order.getProperty())) :
//                                    criteriaBuilder.desc(root.get(order.getProperty())))
//                            .toArray(jakarta.persistence.criteria.Order[]::new)
//            );
//        }
//
//        TypedQuery<Worker> query = entityManager.createQuery(criteriaQuery);
//
//
//        if (pageable.isPaged()) {
//            query.setFirstResult((int) pageable.getOffset());
//            query.setMaxResults(pageable.getPageSize());
//        }
//        return entityManager.createQuery(criteriaQuery).getResultList();
//    }
    public Optional<Worker> findById(Integer id) {
        Worker worker = entityManager.find(Worker.class, id);
        return Optional.ofNullable(worker);
    }
    public Integer countWorkersByEndDateAndCondition(LocalDate endDate, String condition) {
        Optional<Integer> countOptional;
        if ("greater".equals(condition)) {
            countOptional = countWorkersByEndDateGreaterThan(endDate);
        } else {
            countOptional = countWorkersByEndDateEquals(endDate);
        }
        return countOptional.orElse(0);
    }

    @SuppressWarnings("unchecked")
    public Optional<Integer> countWorkersByEndDateGreaterThan(LocalDate endDate) {
        Query query = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM worker WHERE end_date > :endDate"
        );
        query.setParameter("endDate", endDate);
        List<Long> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0).intValue());
    }

    @SuppressWarnings("unchecked")
    public Optional<Integer> countWorkersByEndDateEquals(LocalDate endDate) {
        Query query = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM worker WHERE end_date = :endDate"
        );
        query.setParameter("endDate", endDate);
        List<Long> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0).intValue());
    }

    public Optional<Worker> findWorkerByMaxSalary() {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM worker WHERE salary = (SELECT MAX(salary) FROM worker) ORDER BY id LIMIT 1",
                Worker.class
        );
        List<Worker> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}