package com.example.ttcn2etest.mocktest.user_exam.repository;

import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResults;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserResultsRepository extends JpaRepository<UserResults, String>  , JpaSpecificationExecutor<UserResults> {

//    List<UserResults> findUserResultsByUserResponse(Specification<UserResults> specification ,UserResponse response);



//    List<UserResults> findListResultsByUserResponse (Specification<UserResults> specification);

}
