package com.example.ttcn2etest.mocktest.question.service;

import com.example.ttcn2etest.mocktest.answer.entity.Answer;
import com.example.ttcn2etest.mocktest.answer.repository.AnswerRepository;
import com.example.ttcn2etest.mocktest.question.dto.QuestionResultDTO;
import com.example.ttcn2etest.mocktest.question.repository.QuestionRepository;
import com.example.ttcn2etest.mocktest.question.request.CreateQuestionRequest;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import com.example.ttcn2etest.mocktest.section.repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service

public class QuestionServiceImplm implements QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final SectionRepository sectionRepository ;

    @Autowired
    public QuestionServiceImplm(QuestionRepository questionRepository, AnswerRepository answerRepository, SectionRepository sectionRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override

    public Question createQuestion(CreateQuestionRequest request) {
        //Tạo mới 1 câu hỏi
        Question question = new Question();
        question.setQuestionType(request.getType());
        question.setDescription(request.getDescription());
        question.setContent(request.getContent());
//        Kiểm tra danh sách đáp án gửi lên có null không . Danh sách đáp án đúng null là câu hỏi tự luận , danh sách đáp án  đúng không null  là câu hỏi trawsc nghiệm
        // Nếu danh sách đáp án đúng không null thì lưu vào đb
        if (request.getChoiceCorrect() != null) {
            List<Integer> choiceCorrectList = request.getChoiceCorrect();
            List<Integer> choiceCorrect = new ArrayList<>(choiceCorrectList);
            question.setChoiceCorrect(choiceCorrect);
        } else {
            // Nếu danh sách câu hỏi null thì lưu db là null
            question.setChoiceCorrect(null);
        }

        question.setPoint(request.getPoint());
        questionRepository.save(question);

//        Kiểm tra danh sách đáp án không null thì tạo câu hỏi mới . Lấy key của câu hỏi là index được đánh số từ 0 và lưu vào db
        if (request.getListAnswer() != null) {
            AtomicInteger index = new AtomicInteger();

            List<Answer> listAnswer = new ArrayList<>();
            for (CreateQuestionRequest.AnswerReq aw : request.getListAnswer()) {
                Answer answer = new Answer();
                answer.setAnswer(aw.getAnswer());
                answer.setAnswerKey(index.getAndIncrement());
                answer.setQuestion(question);
                listAnswer.add(answer);
            }
            answerRepository.saveAll(listAnswer);
        }
//        questionRepository.save(question);
        return question;
    }


    @Override
    public Question updateQuestion(CreateQuestionRequest request) {
        Optional<Question> question = questionRepository.findById(request.getId());
        if (!question.isPresent()) {
            throw new RuntimeException("Không tồn tại id");
        }
        question.get().setQuestionType(request.getType());
        question.get().setDescription(request.getDescription());
        question.get().setContent(request.getContent());
//       question.setChoiceCorrect(request.getListChoiceCorrect());
        List<Integer> choiceCorrectList = request.getChoiceCorrect();
        List<Integer> choiceCorrect = new ArrayList<>(choiceCorrectList);
        question.get().setChoiceCorrect(choiceCorrect);
        question.get().setPoint(request.getPoint());
        questionRepository.save(question.get());
        AtomicInteger index = new AtomicInteger();

        List<Answer> listAnswer = new ArrayList<>();
        for (CreateQuestionRequest.AnswerReq aw : request.getListAnswer()) {
            List<Answer> answers = answerRepository.findAnswersByQuestion(question.get());
            answerRepository.deleteAll(answers);
            Answer answer = new Answer();
            answer.setAnswer(aw.getAnswer());
            answer.setAnswerKey(index.getAndIncrement());
            answer.setQuestion(question.get());
            listAnswer.add(answer);
        }
        answerRepository.saveAll(listAnswer);
        questionRepository.save(question.get());
        return question.get();
    }

    @Override
    public boolean deleteQuestion(String id) {
        Optional<Question> question = questionRepository.findById(id);
        if (!question.isPresent()) {
            throw new RuntimeException("Không tồn tại id");
        }
        List<Answer> answers = answerRepository.findAnswersByQuestion(question.get());
        answerRepository.deleteAll(answers);
        questionRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Question> listQuestion() {
        return questionRepository.findAll();
    }

    @Override
    @Transactional
    public Question createQuestionInSection(CreateQuestionRequest request, Section section) {
        Question question = new Question();
        question.setQuestionType(request.getType());
        question.setDescription(request.getDescription());
        question.setSection(section);
        question.setContent(request.getContent());
//        Kiểm tra danh sách đáp án gửi lên có null không . Danh sách đáp án đúng null là câu hỏi tự luận , danh sách đáp án  đúng không null  là câu hỏi trawsc nghiệm
        // Nếu danh sách đáp án đúng không null thì lưu vào đb
        if (request.getChoiceCorrect() != null) {
            List<Integer> choiceCorrectList = request.getChoiceCorrect();
            List<Integer> choiceCorrect = new ArrayList<>(choiceCorrectList);
            question.setChoiceCorrect(choiceCorrect);
        } else {
            // Nếu danh sách câu hỏi null thì lưu db là null
            question.setChoiceCorrect(null);
        }

        question.setPoint(request.getPoint());
        questionRepository.save(question);

//        Kiểm tra danh sách đáp án không null thì tạo câu hỏi mới . Lấy key của câu hỏi là index được đánh số từ 0 và lưu vào db
        if (request.getListAnswer() != null) {
            AtomicInteger index = new AtomicInteger();

            List<Answer> listAnswer = new ArrayList<>();
            for (CreateQuestionRequest.AnswerReq aw : request.getListAnswer()) {
                Answer answer = new Answer();
                answer.setAnswer(aw.getAnswer());
                answer.setAnswerKey(index.getAndIncrement());
                answer.setQuestion(question);
                listAnswer.add(answer);
            }
            answerRepository.saveAll(listAnswer);
        }
//        questionRepository.save(question);
        return question;
    }

    @Override
    public List<QuestionResultDTO> listQuestionResult() {
        return null;
    }


    @Override
    public Question addQuestionInSection(CreateQuestionRequest request) {
        Optional<Section> section = sectionRepository.findById(request.getSection_id());
        if(!section.isPresent()){
            throw new RuntimeException("Không tìm thấy thông tin section");
        }
        Question question = createQuestion(request);
        question.setSection(section.get());
        questionRepository.save(question);
        List<Question> questions = section.get().getQuestions();
        questions.add(question);
        section.get().setQuestions(questions);
        sectionRepository.save(section.get());




        return question;
    }
}
