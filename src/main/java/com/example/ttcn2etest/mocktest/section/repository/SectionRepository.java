package com.example.ttcn2etest.mocktest.section.repository;

import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section , String> {
    List<Section> findSectionsByExam(Exam exam);
}
