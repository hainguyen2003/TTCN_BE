package com.example.ttcn2etest.admissions.repository;

import com.example.ttcn2etest.admissions.entity.Admission;
import com.example.ttcn2etest.admissions.request.FilterAdmissionRequest;
import com.example.ttcn2etest.model.etity.News;
import com.example.ttcn2etest.request.news.FilterNewsRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomAdmissionRepository {

    public static Specification<Admission> filterSpecification(Date dateFrom, Date dateTo,
                                                          FilterAdmissionRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // theo ngày
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            // theo tiêu đề
            if (StringUtils.hasText(request.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + request.getTitle() + "%"));
            }
//           // theo chương trình s
//            if (StringUtils.hasText(request.getProgram())) {
//                predicates.add(criteriaBuilder.like(root.get("program"), "%" + request.getProgram() + "%"));
//            }
//            if (request.getStatus() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
//            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
