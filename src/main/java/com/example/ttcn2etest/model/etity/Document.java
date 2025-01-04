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
@Table(name = "document")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 200)
    private String name;
    @NotBlank
    @Size(max = 5000)
    private String content;
    @Size(max = 2000)
    private String file;
    @Size(max = 2000)
    private String image;
    @Enumerated(EnumType.STRING)
    private Status status;  //tai lieu free hay khong free
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;

    public enum Status {
        FREE, NO_FREE
    }
}
