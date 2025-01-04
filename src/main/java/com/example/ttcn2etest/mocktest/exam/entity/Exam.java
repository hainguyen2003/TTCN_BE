package com.example.ttcn2etest.mocktest.exam.entity;


import com.example.ttcn2etest.mocktest.section.entity.Section;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity

public class Exam {
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private String type;
    private String timeExam;
    private Date createDate ;

    private Boolean isFree  = false;
    @OneToMany(mappedBy = "exam"  , cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Section> sections ;

    @OneToMany (mappedBy = "exam" , cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserResponse> user_exam = new ArrayList<>();



}
