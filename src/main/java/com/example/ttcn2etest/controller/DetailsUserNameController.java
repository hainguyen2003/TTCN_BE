package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.service.auth.DetailsUserNameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class DetailsUserNameController {
    private final DetailsUserNameService detailsUserNameService;

    public DetailsUserNameController(DetailsUserNameService detailsUserNameService) {
        this.detailsUserNameService = detailsUserNameService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        UserDTO userDTO = detailsUserNameService.getUserFromToken(jwtToken);
        return ResponseEntity.ok(userDTO);
    }
}
