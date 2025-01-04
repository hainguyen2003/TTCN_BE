package com.example.ttcn2etest.mocktest.section.entity;

import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@Builder
public class Section {
    @Id

    private String id = " ";


    @Column(name = "title")
    private String title ;
    @Column(columnDefinition = "text")
    private String description ;
    @Column(columnDefinition = "text")
    private String file;

    private String type ;

    @OneToMany(mappedBy = "section")
    @JsonManagedReference
    private List<Question> questions;



    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonBackReference
    private Exam exam;
}
