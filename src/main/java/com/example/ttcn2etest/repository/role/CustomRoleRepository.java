package com.example.ttcn2etest.repository.role;

import com.example.ttcn2etest.model.etity.Permission;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.request.role.FilterRoleRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomRoleRepository {
    public static Specification<Role> filterSpecification(Date dateFrom, Date dateTo,
                                                          FilterRoleRequest request) {
        return ((((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })));
    }
}
