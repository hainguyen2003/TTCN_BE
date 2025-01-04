package com.example.ttcn2etest.repository.slide;

import com.example.ttcn2etest.model.etity.Slide;
import com.example.ttcn2etest.request.slide.FilterSlideRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomSlideRepository {
    public static Specification<Slide> filterSpecification(Date dateFrom, Date dateTo,
                                                           FilterSlideRequest request) {
        return ((((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (StringUtils.hasText(request.getLocation())) {
                predicates.add(criteriaBuilder.like(root.get("location"), "%" + request.getLocation() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })));
    }
}
