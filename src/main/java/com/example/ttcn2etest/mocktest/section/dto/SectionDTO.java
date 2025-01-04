package com.example.ttcn2etest.mocktest.section.dto;

import com.example.ttcn2etest.mocktest.question.dto.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionDTO {
    private String id ;
    private String title ;
    private String file ;
    private String description ;
    private String type ;
    private List<QuestionDTO> questions;

}
