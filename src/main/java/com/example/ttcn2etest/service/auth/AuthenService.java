package com.example.ttcn2etest.service.auth;

import com.example.ttcn2etest.request.auth.LoginRequest;
import com.example.ttcn2etest.request.auth.RegisterRequest;
import com.example.ttcn2etest.response.DashBoardResponse;
import com.example.ttcn2etest.response.LoginResponse;

public interface AuthenService {
    LoginResponse authenticateUser(LoginRequest loginRequest);

    void registerUser(RegisterRequest signUpRequest);

    DashBoardResponse getDashBoard(int year);
}
