package com.exercise.api.repository;

import com.exercise.api.entity.JobData;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JobDataRepository extends JpaRepository<JobData, Long>, JpaSpecificationExecutor<JobData> {

    List<JobData> findAll(Specification<JobData> spec);
}
