package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.ErrorCodeDefs;
import com.example.ttcn2etest.request.auth.LoginRequest;
import com.example.ttcn2etest.request.auth.RegisterRequest;
import com.example.ttcn2etest.response.BaseItemResponse;
import com.example.ttcn2etest.response.BaseResponse;
import com.example.ttcn2etest.response.DashBoardResponse;
import com.example.ttcn2etest.response.LoginResponse;
import com.example.ttcn2etest.service.auth.AuthenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
    private AuthenService authenService;

    @Autowired
    public AuthController(AuthenService authenService) {
        this.authenService = authenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse jwtResponse = authenService.authenticateUser(loginRequest);
        BaseItemResponse<LoginResponse> response = new BaseItemResponse<>();
        response.setData(jwtResponse);
        response.setSuccess(true);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        BaseResponse response = new BaseResponse();
        try {
            authenService.registerUser(signUpRequest);
            response.setSuccess(true);
            response.setStatusCode(200);
        } catch (Exception ex) {
            response.setFailed(ErrorCodeDefs.SERVER_ERROR, ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/{year}")
    public ResponseEntity<?> getDashboard(@PathVariable int year) {
        DashBoardResponse data = authenService.getDashBoard(year);
        return BaseResponse.success(data);

    }
}
