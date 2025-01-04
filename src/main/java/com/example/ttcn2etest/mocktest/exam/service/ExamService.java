package com.example.ttcn2etest.mocktest.exam.service;

import com.example.ttcn2etest.mocktest.exam.dto.DetailExamDTO;
import com.example.ttcn2etest.mocktest.exam.dto.ExamDTO;
import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.exam.request.ExamRequest;
import com.example.ttcn2etest.mocktest.section.dto.SectionDTO;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import com.example.ttcn2etest.mocktest.section.request.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ExamService {
    Exam createExam(ExamRequest request);

    ExamDTO updateExam(ExamRequest request);

    boolean deleteExam(String id);

    ResponseEntity<?> getAllExamFree();

    ResponseEntity<?> getAllExam();

    DetailExamDTO getByID(String id);

    List<DetailExamDTO> listDetailExam();

    ExamDTO addSectionToExam(SectionRequest sectionRequest);

    List<SectionDTO> findQuestionByType(String id , String type) ;

    Exam createExamByExcel(String path) ;

    ResponseEntity<?> getListExamByService(long userid) ;

    ResponseEntity<?> readExamFromExcel(MultipartFile file);

    ResponseEntity<?> findExamByName (String name) ;

    ResponseEntity<?> getExamByType (String type , boolean isFree ) ;

    ResponseEntity<?> getDetailByExamId (String examId);

    ResponseEntity<?> getExamById(String id );
}
