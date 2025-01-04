package com.example.ttcn2etest.model.etity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Proxy(lazy = false)
@Table(name = "class_user")
public class ClassUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "class_id", nullable = false)
    private String classId;

    @JoinColumn(name = "id_user", nullable = false)
    private Long idUser;


}