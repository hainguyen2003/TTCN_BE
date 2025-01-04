package com.example.ttcn2etest.mocktest.answer.service;

import com.example.ttcn2etest.mocktest.answer.entity.Answer;
import com.example.ttcn2etest.mocktest.answer.repository.AnswerRepository;
import com.example.ttcn2etest.mocktest.answer.request.AnswerRequest;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.question.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Answer createAnswer(AnswerRequest request) {
        Answer answer = new Answer();
        Optional<Question> question = questionRepository.findById(request.getQuestionID());
        if(!question.isPresent()){
            throw new RuntimeException("Id không tồn tại ");
        }
        answer.setAnswerKey(request.getKey());
        answer.setQuestion(question.get());
        answer.setAnswer(request.getAnswer());
        answerRepository.save(answer);

        return answer;
    }

    @Override
    public Answer updateAnswer(AnswerRequest request) {
        Optional<Answer> answer = answerRepository.findById(request.getAnswerId());
        if(!answer.isPresent()){
            throw new RuntimeException("Không tìm thấy id của câu hỏi ");
        }
        answer.get().setAnswer(request.getAnswer());
        answerRepository.save(answer.get());
        return answer.get();
    }

    @Override
    public boolean deleteAnswer(String id) {
        answerRepository.deleteById(id);
        return true;
    }
}
