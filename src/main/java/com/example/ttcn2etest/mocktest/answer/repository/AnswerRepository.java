package com.example.ttcn2etest.mocktest.answer.repository;

import com.example.ttcn2etest.mocktest.answer.entity.Answer;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository <Answer , String> {
    List<Answer> findAnswersByQuestion(Question question);
}
