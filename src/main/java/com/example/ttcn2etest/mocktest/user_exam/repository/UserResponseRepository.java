package com.example.ttcn2etest.mocktest.user_exam.repository;

import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserResponseRepository  extends JpaRepository<UserResponse , String> , JpaSpecificationExecutor<UserResponse> {
    Optional<UserResponse> findUserResponseByUserIdAndExam(long userId , Exam exam);

    Optional<UserResponse> findUserResponseByEmailAndExam(String email , Exam exam);
    List<UserResponse> findUserResponsesByUserId(long userId);

    List<UserResponse> findUserResponsesByEmail(String email);
}
