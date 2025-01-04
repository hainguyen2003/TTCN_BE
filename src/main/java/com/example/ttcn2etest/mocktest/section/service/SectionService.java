package com.example.ttcn2etest.mocktest.section.service;

import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import com.example.ttcn2etest.mocktest.section.request.SectionRequest;

import java.util.List;
import java.util.UUID;


public interface SectionService {
     Section createSection(SectionRequest request);

     Section updateSection (SectionRequest request);

     boolean deleteSection (String id);

     List<Section> getAllSection();

     Section createSectionInExam(SectionRequest sectionRequest );




}
