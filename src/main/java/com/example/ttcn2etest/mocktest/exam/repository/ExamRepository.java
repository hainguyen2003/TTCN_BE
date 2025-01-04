package com.example.ttcn2etest.mocktest.exam.repository;

import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, String> {
    Exam findExamById(String id );
    @Query("SELECT e FROM Exam e WHERE UPPER(e.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    List<Exam> listExamByName(String name);

    List<Exam> findExamsByType(String type);

}
