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
@Table(name = "display_manager")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisplayManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 5000)
    private String description;
    @NotBlank
    @Size(max = 2000)
    private String image;
    @Size(max = 100)
    private String location;
    @Size(max = 50)
    private String type;

    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;

}
    