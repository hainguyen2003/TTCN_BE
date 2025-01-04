package com.example.ttcn2etest.repository.service;

import com.example.ttcn2etest.model.etity.Service;
import com.example.ttcn2etest.request.service.FilterServiceRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomServiceRepository {
    public static Specification<Service> filterSpecification(Date dateFrom, Date dateTo,
                                                             BigDecimal minPrice, BigDecimal maxPrice,
                                                             FilterServiceRequest request
    ) {
        return ((((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Logic lọc theo khoảng thời gian đã tồn tại
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }

            // Lọc theo tên dịch vụ
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            // Lọc theo loại dịch vụ
            if (request.getTypeOfService() != null && !request.getTypeOfService().equals("")) {
                try {
                    Service.TypeService typeService = Service.TypeService.valueOf(String.valueOf(request.getTypeOfService()));
                    predicates.add(criteriaBuilder.equal(root.get("typeOfService"), typeService));
                } catch (Exception e) {
                    throw new RuntimeException("Loại dịch vụ tìm kiếm không tồn tại!");
                }
            }

            // Lọc theo hình thức học
            if (request.getLearnOnlineOrOffline() != null && !request.getLearnOnlineOrOffline().equals("")) {
                try {
                    Service.Learn learn = Service.Learn.valueOf(String.valueOf(request.getLearnOnlineOrOffline()));
                    predicates.add(criteriaBuilder.equal(root.get("learnOnlineOrOffline"), learn));
                } catch (Exception e) {
                    throw new RuntimeException("Hình thức học tìm kiếm không tồn tại!");
                }
            }

            // Lọc theo giá
            if (minPrice != null && maxPrice != null) {
                predicates.add(criteriaBuilder.between(root.get("coursePrice"), minPrice, maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })));
    }
}
