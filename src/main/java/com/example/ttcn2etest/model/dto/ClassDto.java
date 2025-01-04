package com.example.ttcn2etest.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.ttcn2etest.model.etity.Class}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDto implements Serializable {
    @Size(max = 10)
    String classId;
    Long courseId;
    @NotNull
    @Size(max = 255)
    String className;
    @NotNull
    @Size(max = 255)
    String teacherName;
    @NotNull
    @Size(max = 255)
    String schedule;
    @NotNull
    LocalDate startDate;
    @NotNull
    LocalDate endDate;
    Instant createdAt;
    Instant updatedAt;
}