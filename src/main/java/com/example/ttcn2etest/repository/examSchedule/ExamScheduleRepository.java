package com.example.ttcn2etest.repository.examSchedule;

import com.example.ttcn2etest.model.etity.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long>, JpaSpecificationExecutor<ExamSchedule> {
}
