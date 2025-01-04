package com.example.ttcn2etest.mocktest.section.service;

import com.example.ttcn2etest.mocktest.answer.repository.AnswerRepository;
import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.question.repository.QuestionRepository;
import com.example.ttcn2etest.mocktest.question.request.CreateQuestionRequest;
import com.example.ttcn2etest.mocktest.question.service.QuestionService;
import com.example.ttcn2etest.mocktest.section.repository.SectionRepository;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import com.example.ttcn2etest.mocktest.section.request.SectionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionServiceImplm implements SectionService {
    private final SectionRepository sectionRepository;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;


    private final QuestionRepository questionRepository;
    private static final Logger logger = Logger.getLogger(SectionServiceImplm.class.getSimpleName());




    @Override
    public Section createSection(SectionRequest request) {
        Section section = new Section();
        section.setTitle(request.getTitle());
        sectionRepository.save(section);
        List<Question> questions = new ArrayList<>();
        questionRepository.saveAll(questions);
        for (CreateQuestionRequest item : request.getQuestions()) {
            Question question = new Question();
            question.setSection(section);
            question = questionService.createQuestionInSection(item, section);
            questionRepository.save(question);
            questions.add(question);


        }
        section.setQuestions(questions);


        return section;
    }

    @Override
    public Section updateSection(SectionRequest request) {
        Optional<Section> section = sectionRepository.findById(request.getId());
        if (!section.isPresent()) {
            throw new RuntimeException("Id không tồn tại");
        }
        section.get().setTitle(request.getTitle());
        section.get().setType(request.getType());
        section.get().setDescription(request.getDescription());
        section.get().setFile(request.getFile());
        sectionRepository.save(section.get());
        return section.get();
    }

    @Override
    public boolean deleteSection(String id) {
        Optional<Section> section = sectionRepository.findById(id);
        if (!section.isPresent()) {
            throw new RuntimeException("Id không tồn tại");
        }

        List<Question> questions =  questionRepository.findQuestionsBySection(section.get());
        questionRepository.deleteAll(questions);
        sectionRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Section> getAllSection() {
        return sectionRepository.findAll();
    }

    @Override
    @Transactional
    public Section createSectionInExam(SectionRequest sectionRequest) {
        Section section = new Section();
        section.setTitle(sectionRequest.getTitle());
        section.setDescription(sectionRequest.getDescription());
        section.setType(sectionRequest.getType());
        section.setFile(sectionRequest.getFile());
        sectionRepository.save(section);
        List<Question> questions = new ArrayList<>();
        questionRepository.saveAll(questions);
        for (CreateQuestionRequest item : sectionRequest.getQuestions()) {
            Question question = new Question();
            question.setSection(section);
            question = questionService.createQuestionInSection(item, section);
            questionRepository.save(question);
            questions.add(question);


        }
        section.setQuestions(questions);


        return section;
    }
}
