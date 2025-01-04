package com.example.ttcn2etest.mocktest.user_exam.repository;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResponse;
import com.example.ttcn2etest.mocktest.user_exam.entity.UserResults;
import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResultsRequest;
import com.example.ttcn2etest.utils.MyUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomUserResultsRepository {
    public static Specification<UserResults> filterSpecification(FilterUserResultsRequest request) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(StringUtils.hasText(request.getDateFrom()) && StringUtils.hasText(request.getDateTo())  ){
                try {
                    Date dFrom = MyUtils.convertDateFromString(request.getDateFrom() , DateTimeConstant.DATE_FORMAT);
                    Date dTo = MyUtils.convertDateFromString(request.getDateTo() , DateTimeConstant.DATE_FORMAT);
                    predicates.add(criteriaBuilder.between(root.get("createDate") , dFrom ,dTo) );

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
            if(request.isHighToLow()) {
                query.orderBy(criteriaBuilder.desc(root.get("totalPoint")));
            }else {
                query.orderBy(criteriaBuilder.asc(root.get("totalPoint")));
            }

            if(StringUtils.hasText(request.getUserResponseId())){
                predicates.add(criteriaBuilder.equal(root.get("userResponse").get("id") ,request.getUserResponseId() )) ;
            }
            if(request.isSortDateDESC()){
                query.orderBy(criteriaBuilder.asc(root.get("createDate")));
            }
            if (StringUtils.hasText(request.getExamId())) {
                Join<UserResponse, Exam> examJoin = root.join("userResponse").join("exam");
                predicates.add(criteriaBuilder.equal(examJoin.get("id"), request.getExamId()));
            }



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }

}
