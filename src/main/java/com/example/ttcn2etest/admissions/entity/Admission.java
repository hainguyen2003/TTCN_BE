package com.example.ttcn2etest.admissions.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "admission")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 1000)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String program;

    @Size(max = 10000)
    private String description;

    @Column(name = "admission_form")
    @Size( max = 10000)
    private String admissionForm;

    @Size(max = 2000)
    private String image;

    @Column(name = "link_register")
    @Size(max = 2000)
    private String linkRegister;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @Column(name = "status", nullable = false)
    private Boolean status;

    //getter + setter


}