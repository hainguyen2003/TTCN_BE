package com.example.ttcn2etest.mocktest.user_exam.entity;


import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
public class
UserResponse {
    @Id
    private String id = UUID.randomUUID().toString();

    private int count = 0;

    private int maxCount = 5;
    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonBackReference
    private Exam exam;

    private String email ;
    private Long userId ;
    @OneToMany(mappedBy = "userResponse" ,cascade = CascadeType.ALL)
    @JsonManagedReference
    List<UserResults> responseUsers ;







}
