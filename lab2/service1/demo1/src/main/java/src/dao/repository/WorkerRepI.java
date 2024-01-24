package src.dao.repository;

import src.dao.model.Worker;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkerRepI {
    Optional<Worker> findWorkerByMaxSalary();
    Optional<Integer> countWorkersByEndDateEquals(LocalDate endDate);
    Optional<Integer> countWorkersByEndDateGreaterThan(LocalDate endDate);
    Integer countWorkersByEndDateAndCondition(LocalDate endDate, String condition);
    Optional<Worker> findById(Integer id);
    boolean existsById(Integer id);
    void delete(Integer id);
    Worker saveAndFlush(Worker worker);
    Worker create(Worker worker);
}
