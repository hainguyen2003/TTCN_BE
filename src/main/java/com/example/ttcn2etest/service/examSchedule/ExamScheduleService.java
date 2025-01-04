package com.example.ttcn2etest.service.examSchedule;

import com.example.ttcn2etest.model.dto.ExamScheduleDTO;
import com.example.ttcn2etest.model.etity.ExamSchedule;
import com.example.ttcn2etest.request.examSchedule.CreateExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.FilterExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.UpdateExamScheduleRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExamScheduleService {
    List<ExamScheduleDTO> getALLExamSchedule();

    ExamScheduleDTO getByIdExamSchedule(Long id);

    ExamScheduleDTO createExamSchedule(CreateExamScheduleRequest request);

    ExamScheduleDTO updateExamSchedule(UpdateExamScheduleRequest request, Long id);

    ExamScheduleDTO deleteByIdExamSchedule(Long id);

    List<ExamScheduleDTO> deleteAllExamSchedule(List<Long> ids);

    Page<ExamSchedule> filterExamSchedule(FilterExamScheduleRequest request);

}
