package com.example.ttcn2etest.repository.displayManager;

import com.example.ttcn2etest.model.etity.DisplayManager;
import com.example.ttcn2etest.request.displayManager.FilterDisplayRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomDisplayManagerRepository {
    public static Specification<DisplayManager> filterSpecification(Date dateFrom, Date dateTo,
                                                                    FilterDisplayRequest request) {
        return ((((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
//            if (dateFrom != null && dateTo != null) {
//                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
//            }
//            if (StringUtils.hasText(request.getTitle())) {
//                predicates.add(criteriaBuilder.like(root.get("title"), "%" + request.getTitle() + "%"));
//            }
//            if (StringUtils.hasText(request.getLocation())) {
//                predicates.add(criteriaBuilder.like(root.get("location"), "%" + request.getLocation() + "%"));
//            }
//            if (StringUtils.hasText(request.getType())) {
//                predicates.add(criteriaBuilder.like(root.get("type"), "%" + request.getType() + "%"));
//            }
            if (StringUtils.hasText(request.getTitle()) && !"undefined".equals(request.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + request.getTitle() + "%"));
            }
            if (StringUtils.hasText(request.getLocation()) && !"undefined".equals(request.getLocation())) {
                predicates.add(criteriaBuilder.like(root.get("location"), "%" + request.getLocation() + "%"));
            }
            if (StringUtils.hasText(request.getType()) && !"undefined".equals(request.getType())) {
                predicates.add(criteriaBuilder.like(root.get("type"), "%" + request.getType() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })));
    }
}
