package com.example.ttcn2etest.mocktest.user_exam.repository;

import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.exam.repository.ExamRepository;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResponseRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomUserResponseRepository {

    public static Specification<UserResponse> filterUserResponse(FilterUserResponseRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }
//            if (StringUtils.hasText(request.getExamName())) {
//                Join<UserResponse, Exam> examJoin = root.join("userResponse").join("exam");
//                predicates.add(criteriaBuilder.like(examJoin.get("name"), "%" + request.getExamName() + "%"));
//            }
            if (StringUtils.hasText(request.getExamName())) {
                Join<UserResponse, Exam> examJoin = root.join("exam");
                predicates.add(criteriaBuilder.like(examJoin.get("name"), "%" + request.getExamName() + "%"));
            }
            if (request.isSortCountExam() == true) {
                query.orderBy(criteriaBuilder.desc(root.get("count")));
            }else {
                query.orderBy(criteriaBuilder.asc(root.get("count")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
