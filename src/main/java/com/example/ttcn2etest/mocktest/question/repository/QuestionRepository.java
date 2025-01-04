package com.example.ttcn2etest.mocktest.question.repository;

import com.example.ttcn2etest.mocktest.question.dto.QuestionResultDTO;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question , String> {
   List<Question> findQuestionsBySection(Section  section);
   Question findQuestionById(UUID id);

}
