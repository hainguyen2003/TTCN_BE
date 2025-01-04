package com.example.ttcn2etest.admissions.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAdmissionRequest {

    @NotBlank(message = "Title cannot be empty!")
    @Size(min = 6, max = 1000, message = "Title must be between 6 and 100 characters!")
    private String title;

    @NotBlank(message = "Program cannot be empty!")
    @Size(max = 2000, message = "Program cannot exceed 800 characters!")
    private String program;

    @Size(max = 10000, message = "Description cannot exceed 5000 characters!")
    private String description;

    @Size(max = 10000, message = "Admission form cannot exceed 600 characters!")
    private String admissionForm;

    @NotBlank(message = "Image cannot be empty!")
    @Size(max = 2000, message = "Image URL cannot exceed 2000 characters!")
    private String image;

    @Size(max = 2000, message = "Registration link cannot exceed 2000 characters!")
    private String linkRegister;

    @NotNull(message = "status ẩn / hiện")
    private Boolean status;

}
