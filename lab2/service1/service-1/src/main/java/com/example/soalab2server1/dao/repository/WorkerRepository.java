//package com.example.soalab2server1.dao.repository;
//
//import com.example.soalab2server1.dao.model.Worker;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//@Repository
//public interface WorkerRepository extends JpaRepository<Worker,Integer>, JpaSpecificationExecutor<Worker> {
//
//    default Integer countWorkersByEndDateAndCondition(LocalDate endDate, String condition){
//        Optional<Integer> countOptional;
//        if (Objects.equals(condition, "greater")) {
//            countOptional = countWorkersByEndDateGreaterThan(endDate);
//        } else {
//            countOptional = countWorkersByEndDateEquals(endDate);
//        }
//        return countOptional.orElse(0);
//    };
//
//    @Query(value = "select count (*) from worker where end_date>:endDate",
//            nativeQuery = true
//    )
//    Optional<Integer> countWorkersByEndDateGreaterThan(@Param("endDate") LocalDate endDate);
//    @Query(value = "select count (*) from worker where end_date=:endDate",
//            nativeQuery = true
//    )
//    Optional<Integer> countWorkersByEndDateEquals(@Param("endDate") LocalDate endDate);
//    @Query(value = "select * from worker where salary = (select max(salary) from worker) order by id limit 1 ",
//            nativeQuery = true
//    )
//    Optional<Worker> findWorkerByMaxSalary();
//}