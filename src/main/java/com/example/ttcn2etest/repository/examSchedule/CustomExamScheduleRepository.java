package com.example.ttcn2etest.repository.examSchedule;

import com.example.ttcn2etest.model.etity.ExamSchedule;
import com.example.ttcn2etest.request.examSchedule.FilterExamScheduleRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomExamScheduleRepository {
    public static Specification<ExamSchedule> filterSpecification(FilterExamScheduleRequest request) {
        return ((((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getAreaId() != null && !request.getAreaId().equals("")) {
                try {
                    ExamSchedule.Area areaId = ExamSchedule.Area.valueOf(String.valueOf(request.getAreaId()));
                    predicates.add(criteriaBuilder.equal(root.get("areaId"), areaId));
                } catch (Exception e) {
                    throw new RuntimeException("Khu vực tìm kiếm không hợp lệ!");
                }
            }
            if (StringUtils.hasText(request.getNameArea())) {
                predicates.add(criteriaBuilder.like(root.get("nameArea"), "%" + request.getNameArea() + "%"));
            }
            if (StringUtils.hasText(request.getSchoolId())) {
                predicates.add(criteriaBuilder.like(root.get("schoolId"), "%" + request.getSchoolId() + "%"));
            }
            if (StringUtils.hasText(request.getNameExamSchool())) {
                predicates.add(criteriaBuilder.like(root.get("nameExamSchool"), "%" + request.getNameExamSchool() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })));
    }
}
