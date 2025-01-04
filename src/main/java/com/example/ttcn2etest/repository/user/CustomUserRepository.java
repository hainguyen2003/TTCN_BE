package com.example.ttcn2etest.repository.user;

import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.request.user.FilterUserRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class CustomUserRepository {
    public static Specification<User> filterSpecification(Date dateFrom, Date dateTo,
                                                          Date dobFrom, Date dobTo,
                                                          FilterUserRequest request) {
        return ((((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

//            predicates.add(criteriaBuilder.equal(root.get("isSuperAdmin"), false));
            predicates.add(criteriaBuilder.equal(root.get("isVerified"),false));
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (dobFrom != null && dobTo != null) {
                predicates.add(criteriaBuilder.between(root.get("dateOfBirth"), dobFrom, dobTo));
            }
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if (StringUtils.hasText(request.getUserName())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + request.getUserName() + "%"));
            }
            if (StringUtils.hasText(request.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
            }
            if (StringUtils.hasText(request.getAddress())) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + request.getAddress() + "%"));
            }
            if (StringUtils.hasText(request.getRoleId())) {
                predicates.add(criteriaBuilder.like(root.get("role"), "%" + request.getRoleId() + "%"));
            }

//            if(StringUtils.hasText(request.getServiceId())){
//                predicates.add(criteriaBuilder.like(root.get("services"),"%" + request.getServiceId()+"%"));
//            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })));
    }
}
