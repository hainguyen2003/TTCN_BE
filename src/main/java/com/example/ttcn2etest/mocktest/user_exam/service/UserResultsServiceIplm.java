package com.example.ttcn2etest.mocktest.user_exam.service;

import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.exam.repository.ExamRepository;
import com.example.ttcn2etest.mocktest.question.dto.QuestionDTO;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.question.repository.QuestionRepository;
import com.example.ttcn2etest.mocktest.user_exam.dto.DetailResults;
import com.example.ttcn2etest.mocktest.user_exam.dto.UserResultsDTO;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResults;
import com.example.ttcn2etest.mocktest.user_exam.repository.CustomUserResultsRepository;
import com.example.ttcn2etest.mocktest.user_exam.repository.UserResponseRepository;
import com.example.ttcn2etest.mocktest.user_exam.repository.UserResultsRepository;
import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.StatisticResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResultsRequest;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.response.BaseItemResponse;
import com.example.ttcn2etest.response.BaseListItemResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserResultsServiceIplm implements UserResultsService {
    private final UserResultsRepository userResultsRepository;
    private final UserResponseServiceImplm userResponseServiceImplm;
    private final QuestionRepository questionRepository;
    private final UserResponseRepository userResponseRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final ExamRepository examRepository;

    @Override
    public ResponseEntity<?> getUserResultsDetail(String id) {
        Optional<UserResults> results = userResultsRepository.findById(id);
        if (!results.isPresent()) {
            throw new RuntimeException("Khong tim thay id");
        }
        UserResults userResults = results.get();
        List<UserResultsRequest> userResultsRequests = userResponseServiceImplm.convertStringToListUserResults(userResults.getResults());
        List<DetailResults> detailResults = new ArrayList<>();
        for (UserResultsRequest resultsRequest : userResultsRequests) {
            Optional<Question> question = questionRepository.findById(resultsRequest.getQuestionId());
            if (question.isPresent()) {
                DetailResults detailResults1 = DetailResults.builder()
                        .choiceCorrect(question.get().getChoiceCorrect())
                        .choiceUser(resultsRequest.getAnswerKey())
                        .question(mapper.map(question.get(), QuestionDTO.class))
                        .build();
                detailResults.add(detailResults1);
            }
        }

        BaseItemResponse baseItemResponse = new BaseItemResponse();
        baseItemResponse.setSuccess();
        baseItemResponse.setData(UserResultsDTO.builder()
                .comment(userResults.getComment())
                .pointListening(userResults.getPointListening())
                        .pointReading(userResults.getPointReading())
                        .pointSpeaking(userResults.getPointSpeaking())
                        .pointWriting(userResults.getPointWriting())
                .id(userResults.getId())
                .detailResults(detailResults)
                .build());
        return ResponseEntity.ok(baseItemResponse);
    }

    @Override
    public ResponseEntity<?> getListUserResults(UserResultsRequest resultsRequest) {
        List<UserResultsDTO> results = new ArrayList<>();

        List<UserResponse> userResponseList = null;
        if (StringUtils.hasText(resultsRequest.getEmail())) {
            userResponseList = userResponseRepository.findUserResponsesByEmail(resultsRequest.getEmail());
        }

        if (resultsRequest.getId() != 0L) {
            userResponseList = userResponseRepository.findUserResponsesByUserId(resultsRequest.getId());
        }
        ;
        for (UserResponse response : userResponseList) {
            results.addAll(response.getResponseUsers().stream().map((rs) -> {
                UserResultsDTO resultsDTO = mapper.map(rs, UserResultsDTO.class);
                resultsDTO.setNameExam(response.getExam().getName());
                resultsDTO.setTime(response.getExam().getTimeExam());
                resultsDTO.setKey(resultsDTO.getId());
                return resultsDTO;
            }).collect(Collectors.toList()));
        }


        BaseListItemResponse baseItemResponse = new BaseListItemResponse();
        baseItemResponse.setSuccess();
        baseItemResponse.setResult(results, results.size());
        return ResponseEntity.ok(baseItemResponse);
    }

    @Override
    public ResponseEntity<?> listUserResultsByUserResponseId(String userResponseId) {
        Optional<UserResponse> userResponse1 = userResponseRepository.findById(userResponseId);
        if (!userResponse1.isPresent()) {
            throw new RuntimeException("Không tìm thấy id");
        }
        UserResponse userResponse = userResponse1.get();
        List<UserResultsDTO> userResultsDTOS = userResponse.getResponseUsers().stream().map(i -> mapper.map(i, UserResultsDTO.class)).collect(Collectors.toList());
        BaseListItemResponse response = new BaseListItemResponse<>();
        response.setSuccess();
        response.setResult(userResultsDTOS, userResultsDTOS.size());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> sortUserResultsASC(FilterUserResultsRequest request) {
        UserResponse response = userResponseRepository.findById(request.getUserResponseId()).orElseThrow(() -> new RuntimeException("user id không tồn tại"));
//       List<UserResults> userResults = userResultsRepository.findUserResultsByUserResponse(response);

//       //Sắp xếp theo điểm từ cao tới thấp
//       if(request.isHighToLow() == true){
//        return ResponseEntity.ok(userResults.stream().sorted(Comparator.comparing(UserResults :: getPoint).reversed()).map(i-> mapper.map(i , UserResultsDTO.class)).collect(Collectors.toList()));
//       }
//       // trả về danh sách kết quả theo ngày gần nhất của userResponse
//       return ResponseEntity.ok( userResults.stream().sorted(Comparator.comparing(UserResults :: getCreateDate).reversed()).map(i-> mapper.map(i , UserResultsDTO.class)).collect(Collectors.toList()) );


        return null;
    }

    @Override
    public ResponseEntity<?> sortUserResultsDESC(FilterUserResultsRequest request) {
        UserResponse response = userResponseRepository.findById(request.getUserResponseId()).orElseThrow(() -> new RuntimeException("user id không tồn tại"));
        Specification<UserResults> specification = CustomUserResultsRepository.filterSpecification(request);

        // Tạo một đối tượng Pageable để chứa thông tin sắp xếp
        Sort sort = Sort.by(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);

        // Sử dụng Pageable trong findAll
        List<UserResultsDTO> sortedResults = userResultsRepository.findAll(specification).stream().map(i -> mapper.map(i, UserResultsDTO.class)).collect(Collectors.toList());

        BaseListItemResponse response1 = new BaseListItemResponse<>();
        response1.setSuccess();
        response1.setResult(sortedResults, sortedResults.size());

        return ResponseEntity.ok(response1);
    }

    @Override
    public ResponseEntity<?> listResultsByExam(StatisticResultsRequest request) {
        Exam exam = examRepository.findExamById(request.getExamId());
        List<UserResponse> userResponseList = exam.getUser_exam();
        List<UserResults> userResults = new ArrayList<>();
        List<UserResultsDTO> userResultsDTOS = userResults.stream().map(i -> mapper.map(i, UserResultsDTO.class)).collect(Collectors.toList()); ;
        BaseListItemResponse listItemResponse = new BaseListItemResponse();
        listItemResponse.setSuccess();
        for (UserResponse response : userResponseList) {
            userResults.addAll(response.getResponseUsers());
        }
        if (request.isSortCreateDate()) {
            userResultsDTOS = userResults.stream().sorted(Comparator.comparing(UserResults::getCreateDate).reversed())
                    .map(i -> mapper.map(i, UserResultsDTO.class))
                    .collect(Collectors.toList());
            listItemResponse.setResult(userResultsDTOS ,userResultsDTOS.size());
            return ResponseEntity.ok().body(listItemResponse);
        }
        if (request.isSortHighToLow()) {
            userResultsDTOS = userResults.stream().sorted(Comparator.comparing(UserResults::getTotalPoint).reversed())
                    .map(i -> mapper.map(i, UserResultsDTO.class)).collect(Collectors.toList());
            listItemResponse.setResult(userResultsDTOS , userResultsDTOS.size());
            return ResponseEntity.ok().body(listItemResponse);

        }

        listItemResponse.setResult(userResultsDTOS ,userResultsDTOS.size());
        return ResponseEntity.ok().body(listItemResponse);
    }
}
