package com.example.ttcn2etest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String jwt;
    private Long id;
    private String name;
    private String username;
    private String phone;
    private String email;
    private List<String> roles;
}
