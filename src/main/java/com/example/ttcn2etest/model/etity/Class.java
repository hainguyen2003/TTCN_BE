package com.example.ttcn2etest.model.etity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "class")
public class Class {
    @Id
    @Size(max = 10)
    @Column(name = "class_id", nullable = false, length = 10)
    private String classId;

    @Column(name = "course_id", nullable = false, length = 10)
    private Long courseId;

    @Size(max = 255)
    @NotNull
    @Column(name = "class_name", nullable = false)
    private String className;

    @Size(max = 255)
    @NotNull
    @Column(name = "teacher_name", nullable = false)
    private String teacherName;


    @Size(max = 255)
    @NotNull
    @Column(name = "schedule", nullable = false)
    private String schedule;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}