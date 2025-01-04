package com.example.ttcn2etest.mocktest.user_exam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
public class UserResults {
    @Id

    private String id ;
    private Date createDate ;

    @Column(columnDefinition = "text")
    private String results ;
    @Column(name = "resultsWriting")
    private String resultsWriting ;


    @Column(name =  "pointReading")
    private float pointReading ;
    @Column(name =  "pointListening")
    private float pointListening ;
    @Column(name =  "pointWriting")
    private float pointWriting ;
    @Column(name =  "pointSpeaking")
    private float pointSpeaking ;
    @Column(name =  "totalPoint")
    private float totalPoint ;
    @Column(columnDefinition = "text")
    private String comment ;
    @ManyToOne
    @JoinColumn(name = "user_response_id")
    private UserResponse userResponse;
}
