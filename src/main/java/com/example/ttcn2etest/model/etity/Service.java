package com.example.ttcn2etest.model.etity;


import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "service_manager")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 100)
    private String name;
    @Size(max = 1000)
    private String description;
    @Size(max = 2000)
    @Column(name = "detail_description")
    @ElementCollection
    private List<String> detailDescription;
    @Size(max = 300)
    @Column(name = "study_goals")
    private String studyGoals;
    @Column(name = "schedule")
    private String schedule;
    @Column(name = "number_teaching_sessions")
    @Size(max = 100)
    private String numberTeachingSessions;
    @Size(max = 1000)
    @Column(name = "curriculum")
    @ElementCollection
    private List<String> curriculum;
    @Column(name = "learn_online_or_offline")
    @Enumerated(EnumType.STRING)
    private Learn learnOnlineOrOffline;

    @Column(name = "learning_form")
    @Size(max = 300)
    private String learningForm;

    @Column(name = "course_price", precision = 10, scale = 2)
    private BigDecimal coursePrice;
    @Size(max = 100)
    private String price;
    @Column(name = "request_students")
    private String requestStudents;
    @Column(name = "type_service")
    @Enumerated(EnumType.STRING)
    private TypeService typeOfService;       //là dịch vụ chương trình Anh ngữ, Khóa học trong CT hay là lịch ôn tập
    @Size(max = 2000)
    private String image;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;
    @ManyToMany(mappedBy = "services")
    private Collection<User> users;

    public enum TypeService {
        EDUCATION_PROGRAM, REVIEW_LESSON, COURSE, NO_SERVICE
    }

    public enum Learn {
        ONLINE, OFFLINE, ONLINE_AND_OFFLINE
    }

}
