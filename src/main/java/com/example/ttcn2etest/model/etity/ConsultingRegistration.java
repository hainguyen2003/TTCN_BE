package com.example.ttcn2etest.model.etity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "consulting_registration")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultingRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    @Size(max = 15)
    private String phone;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "content_advice")
    private String contentAdvice;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;

    public enum Status {
        CONSULTED, WAITING_FOR_ADVICE   //da dc tu van, cho tu van
    }

}
