package com.example.ttcn2etest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User1DTO {
    private Long userId;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String avatar;
    private Set<Service2DTO> services;
}
