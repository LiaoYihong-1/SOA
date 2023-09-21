package com.example.soalab2server1.dao.repository;

import com.example.soalab2server1.dao.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker,Integer>, JpaSpecificationExecutor<Worker> {

}
