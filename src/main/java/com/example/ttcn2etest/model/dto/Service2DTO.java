package com.example.ttcn2etest.model.dto;

import com.example.ttcn2etest.model.etity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Service2DTO {
    private Long id;
    private String name;
    private String image;
    private Service.Learn learnOnlineOrOffline;
    private String numberTeachingSessions;
    private String price;
}
