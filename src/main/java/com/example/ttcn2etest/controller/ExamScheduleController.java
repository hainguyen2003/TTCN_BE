package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.ExamScheduleDTO;
import com.example.ttcn2etest.model.etity.ExamSchedule;
import com.example.ttcn2etest.request.examSchedule.CreateExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.FilterExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.UpdateExamScheduleRequest;
import com.example.ttcn2etest.service.examSchedule.ExamScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam/schedule")
public class ExamScheduleController extends BaseController {
    private final ExamScheduleService examScheduleService;
    private final ModelMapper modelMapper;

    public ExamScheduleController(ExamScheduleService examScheduleService, ModelMapper modelMapper) {
        this.examScheduleService = examScheduleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getALLExamSchedule() {
        try {
            List<ExamScheduleDTO> response = examScheduleService.getALLExamSchedule();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getByIdExamSchedule(@PathVariable Long id) {
        ExamScheduleDTO response = examScheduleService.getByIdExamSchedule(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> createExamSchedule(@Validated @RequestBody CreateExamScheduleRequest request) {
        ExamScheduleDTO response = examScheduleService.createExamSchedule(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> updateExamSchedule(@Validated @RequestBody UpdateExamScheduleRequest request,
                                         @PathVariable("id") Long id) {
        ExamScheduleDTO response = examScheduleService.updateExamSchedule(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteByIdExamSchedule(@PathVariable Long id) {
        ExamScheduleDTO response = examScheduleService.deleteByIdExamSchedule(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteAllExamSchedule(@RequestBody List<Long> ids) {
        try {
            List<ExamScheduleDTO> response = examScheduleService.deleteAllExamSchedule(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> filterExamScheduleService(@Validated @RequestBody FilterExamScheduleRequest request) {
        Page<ExamSchedule> examSchedulePage = examScheduleService.filterExamSchedule(request);
        List<ExamScheduleDTO> examScheduleDTOS = examSchedulePage.getContent().stream().map(
                examSchedule -> modelMapper.map(examSchedule, ExamScheduleDTO.class)
        ).toList();
        return buildListItemResponse(examScheduleDTOS, examSchedulePage.getTotalElements());
    }

}
