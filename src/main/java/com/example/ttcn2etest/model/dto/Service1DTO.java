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
public class Service1DTO {
    private String id;
    private String name;
    private Service.TypeService typeOfService;
}
