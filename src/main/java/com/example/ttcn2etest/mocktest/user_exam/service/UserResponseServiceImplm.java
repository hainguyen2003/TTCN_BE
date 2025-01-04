package com.example.ttcn2etest.mocktest.user_exam.service;

import com.example.ttcn2etest.mocktest.exam.dto.ExamDTO;
import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.exam.repository.ExamRepository;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.question.repository.QuestionRepository;
import com.example.ttcn2etest.mocktest.user_exam.dto.UserResponseDTO;
import com.example.ttcn2etest.mocktest.user_exam.dto.UserResultsDTO;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResults;
import com.example.ttcn2etest.mocktest.user_exam.repository.CustomUserResponseRepository;
import com.example.ttcn2etest.mocktest.user_exam.repository.UserResponseRepository;
import com.example.ttcn2etest.mocktest.user_exam.repository.UserResultsRepository;
import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResponseRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResponseRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResultsRequest;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.response.BaseItemResponse;
import com.example.ttcn2etest.response.BaseListItemResponse;
import com.example.ttcn2etest.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserResponseServiceImplm implements UserResponseService {
    private final UserResponseRepository userResponseRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final UserResultsRepository userResultsRepository;

    private final QuestionRepository questionRepository;
    private final ModelMapper mapper;


    private static final Logger logger = LogManager.getLogger(UserResponseServiceImplm.class);


    //    @Transactional
    public ResponseEntity<?> addUserResponse(UserResponseRequest request) {
        Exam exam = examRepository.findExamById(request.getExam_id());

        Optional<UserResponse> userResponse =
                (StringUtils.hasText(request.getEmail())) ? userResponseRepository.findUserResponseByEmailAndExam(request.getEmail(), exam) :
                        (request.getUser_id() != 0L) ? userResponseRepository.findUserResponseByUserIdAndExam(request.getUser_id(), exam) : null;


        UserResponse response;
        if (!userResponse.isPresent()) {
            response = UserResponse.builder()
                    .count(0)
                    .maxCount(5)
                    .email(request.getEmail())
                    .id(UUID.randomUUID().toString())
                    .exam(exam)
                    .userId(request.getUser_id())
                    .responseUsers(new ArrayList<>())
                    .build();

//            if(request.getUser_id()!= null){
//                response.setUserId(response.getUserId());
//            }
            userResponseRepository.save(response);
        } else {
            response = userResponse.get();
            response.setCount(response.getCount() + 1);
            if (userResponse.get().getCount() > userResponse.get().getMaxCount()) {
                throw new RuntimeException("Bạn đã vượt quá số lần thi cho phép ");
            }

            userResponseRepository.save(response);
        }

        List<UserResultsRequest> userResultsRequests = new ArrayList<>(request.getResponsereading());
        userResultsRequests.addAll(request.getResponselistening());


        float pointListening = calculatorPoint(request.getResponselistening());
        float pointReading = calculatorPoint(request.getResponsereading());
        float total = pointListening + pointReading;
        UserResults userResults1 = UserResults.builder()
                .userResponse(response)
                .id(UUID.randomUUID().toString())
                .createDate(new Date())
                .pointReading(pointListening)
                .pointListening(pointReading)
                .results(convertUserResultsListToJsonString(userResultsRequests))
                .resultsWriting(convertUserResultsListToJsonString(request.getResponsewriting()))
                .totalPoint(total)
                .build();


        userResults1.setComment(commentResults(userResults1.getTotalPoint()));
        userResultsRepository.save(userResults1);

        response.getResponseUsers().add(userResults1); // Thêm userResults1 vào danh sách đã khởi tạo
        userResponseRepository.save(response);

        BaseItemResponse itemResponse = new BaseItemResponse();
        itemResponse.setSuccess();
        itemResponse.setData(mapper.map(userResults1, UserResultsDTO.class));

        return ResponseEntity.ok(itemResponse);
    }

    public String convertUserResultsListToJsonString(List<UserResultsRequest> userResultsRequestList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(userResultsRequestList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public List<UserResultsRequest> convertStringToListUserResults(String s) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(s, new TypeReference<List<UserResultsRequest>>() {
            });
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String commentResults(Float point) {
        if (point < 4) {
            return "Bài thi của bạn chưa được tốt lắm";
        } else if (point < 8) {
            return "Bạn cần ôn tập thêm các kiến thức";
        } else if (point > 8) {
            return "Bài thi của bạn đạt kết quả tốt";
        } else {
            return "Cạn lời ";
        }
    }


    public float calculatorPoint(List<UserResultsRequest> userResultsRequests) {
        float total = 0;
        for (UserResultsRequest resultsRequest : userResultsRequests) {
            Optional<Question> question = questionRepository.findById(resultsRequest.getQuestionId());
            if (question.isPresent() && resultsRequest.getAnswerKey() != null && question.get().getChoiceCorrect().containsAll(resultsRequest.getAnswerKey()))
                return total += question.get().getPoint();
        }
        return total;
    }


    @Override
    public UserResponseDTO createUserResponse(UserResponseRequest request) {
        return null;
    }

    @Override
    public ResponseEntity updateUserResponse(UserResponseRequest request) {
        Exam exam = examRepository.findExamById(request.getExam_id());
        Optional<UserResponse> userResponse = userResponseRepository.findUserResponseByUserIdAndExam(request.getUser_id(), exam);
        if (!userResponse.isPresent()) {
            throw new RuntimeException("Không tìm thấy userresponse");
        }
        userResponse.get().setCount(request.getCount());
        userResponseRepository.save(userResponse.get());
        return ResponseEntity.ok("Thanh cong cap nhat ");
    }

    @Override
    public boolean deleteUserResponse(UUID id) {
        return false;
    }

    @Override
//    public ResponseEntity<?> listUserResponse() {
//        BaseListItemResponse response = new BaseListItemResponse() ;
//        response.setSuccess();
//        List<UserResponseDTO> userResponseList = userResponseRepository.findAll().
//                stream().map(i ->
//                        {
//                            User user = userRepository.findById(i.getUserId()).orElseThrow(() ->new RuntimeException("Không tìm thấy user "));
////
//
//                            return UserResponseDTO .builder().id(i.getId())
//                                    .count(i.getCount())
//                                    .maxCount(i.getMaxCount())
//                                    .email(i.getEmail())
////                                    .userId(i.getUserId())
//                                    .userName(user.getUsername())
//                                    .examName(i.getExam().getName())
//                                    .build();
//                        }
//                        ).collect(Collectors.toList()) ;
//        response.setResult(userResponseList , userResponseList.size());
//        return ResponseEntity.ok(response);
//    }

    public ResponseEntity<?> listUserResponse() {
        BaseListItemResponse response = new BaseListItemResponse();
        response.setSuccess();
        List<UserResponseDTO> userResponseList = userResponseRepository.findAll()
                .stream()
                .map(i -> {
                    if (i.getUserId() != null) {
                        User user = userRepository.findById(i.getUserId())
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
                        return UserResponseDTO.builder()
                                .id(i.getId())
                                .count(i.getCount())
                                .maxCount(i.getMaxCount())
                                .email(i.getEmail())
                                .userName(user.getUsername())
                                .examName(i.getExam().getName())
                                .build();
                    } else {
                        // Xử lý trường hợp i.getUserId() là null
                        return UserResponseDTO.builder()
                                .id(i.getId())
                                .count(i.getCount())
                                .maxCount(i.getMaxCount())
                                .email(i.getEmail())
                                .userName("N/A") // Hoặc xử lý khác
                                .examName(i.getExam().getName())
                                .build();
                    }
                })
                .collect(Collectors.toList());
        response.setResult(userResponseList, userResponseList.size());
        return ResponseEntity.ok(response);
    }


    @Override
    public UserResponse getUserResponseById(String id) {
        Optional<UserResponse> response = userResponseRepository.findById(id);
        return response.get();
    }

    @Override
    public ResponseEntity<?> updateMaxCount(UserResponseRequest request) {
        UserResponse userResponse = userResponseRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Không tim thấy user response"));
        userResponse.setMaxCount(request.getMaxCount());
        userResponseRepository.save(userResponse);
        return ResponseEntity.ok(mapper.map(userResponse, UserResponseDTO.class));
    }

    @Override
    public ResponseEntity<?> filterUserResponseBycondition(FilterUserResponseRequest request) {
        Specification<UserResponse> specification = CustomUserResponseRepository.filterUserResponse(request);
        List<UserResponseDTO> userResponseList = userResponseRepository.findAll(specification).stream().map(i -> mapper.map(i, UserResponseDTO.class)).collect(Collectors.toList());
        BaseListItemResponse itemResponse = new BaseListItemResponse<>();
        itemResponse.setSuccess();
        itemResponse.setResult(userResponseList, userResponseList.size());
        return ResponseEntity.ok(itemResponse);
    }
}
