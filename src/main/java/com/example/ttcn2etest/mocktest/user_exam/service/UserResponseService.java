package com.example.ttcn2etest.mocktest.user_exam.service;

import com.example.ttcn2etest.mocktest.user_exam.dto.UserResponseDTO;
import com.example.ttcn2etest.mocktest.user_exam.dto.UserResultsDTO;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResponseRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResponseRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserResponseService {
    UserResponseDTO createUserResponse (UserResponseRequest request);
    ResponseEntity updateUserResponse (UserResponseRequest request);

    ResponseEntity addUserResponse(UserResponseRequest request );

    boolean deleteUserResponse(UUID id ) ;





    ResponseEntity<?> listUserResponse();

    UserResponse getUserResponseById(String id) ;

    ResponseEntity<?> updateMaxCount (UserResponseRequest request) ;

    ResponseEntity<?> filterUserResponseBycondition(FilterUserResponseRequest request);

}
