package com.example.ttcn2etest.mocktest.exam.dto;

import com.example.ttcn2etest.mocktest.section.dto.SectionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailExamDTO {
    private String id;
    private String name;
    private String timeExam;
    private boolean isFree ;
    private String typeExam ;
    private List<SectionDTO> sections ;
}
