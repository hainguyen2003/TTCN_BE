package com.example.ttcn2etest.mocktest.user_exam.controller;

import com.example.ttcn2etest.mocktest.user_exam.dto.UserResponseDTO;
import com.example.ttcn2etest.mocktest.user_exam.dto.UserResultsDTO;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResponseRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResponseRequest;
import com.example.ttcn2etest.mocktest.user_exam.service.UserResponseService;
import com.example.ttcn2etest.mocktest.user_exam.service.UserResultsService;
import com.example.ttcn2etest.response.BaseItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("mocktest/userresponse/")
public class UserResponseController {
    private final UserResponseService userResponseService;
    private final UserResultsService userResultsService ;




    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<?> createUserResponse(@RequestBody UserResponseRequest request) {
        UserResponseDTO userResponse = userResponseService.createUserResponse(request);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER')")
    public ResponseEntity<?> getallUserResponse(){
        return userResponseService.listUserResponse();
    }



    @PutMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER')")
    public ResponseEntity<?> updateUserResponse(@RequestBody UserResponseRequest request) {

        return userResponseService.updateUserResponse(request);
    }


    @PutMapping("count")
    @PreAuthorize("hasAnyAuthority('ADMIN' )")
    public ResponseEntity<?> updateMaxCount (@RequestBody UserResponseRequest request){
        return ResponseEntity.ok(userResponseService.updateMaxCount(request));
    }

    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER' )")
    public ResponseEntity<?> filterByCondition (@RequestBody FilterUserResponseRequest request){
        return ResponseEntity.ok(userResponseService.filterUserResponseBycondition(request));
    }





}
