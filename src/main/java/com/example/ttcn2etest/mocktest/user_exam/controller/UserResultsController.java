package com.example.ttcn2etest.mocktest.user_exam.controller;

import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.StatisticResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.service.UserResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mocktest/results/")
@RequiredArgsConstructor
public class UserResultsController {
    private final UserResultsService userResultsService;

    @GetMapping("responseId/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER')")
    public ResponseEntity<?> getAllResultsByResponseId(@PathVariable String id) {
        return ResponseEntity.ok(userResultsService.listUserResultsByUserResponseId(id));

    }
    @PostMapping("/all")
    public ResponseEntity<?> getListResults(@RequestBody UserResultsRequest request){
        return userResultsService.getListUserResults (request);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER')")
    public ResponseEntity<?> getResultsUser(@PathVariable String id){
        return ResponseEntity.ok(userResultsService.getUserResultsDetail(id));
    }
    @PostMapping("filter")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER')")
    public ResponseEntity<?> filterResultsASC(@RequestBody FilterUserResultsRequest request){
        return ResponseEntity.ok(userResultsService.sortUserResultsDESC(request));
    }

    @PostMapping("statistic")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER')")
    public ResponseEntity<?> listResultsByExam(@RequestBody StatisticResultsRequest request){
        return ResponseEntity.ok().body(userResultsService.listResultsByExam(request));
    }

}
