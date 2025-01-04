package com.example.ttcn2etest.repository.document;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.etity.Document;
import com.example.ttcn2etest.request.document.FilterDocumentRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomDocumentRepository {
    public static Specification<Document> filterSpecification(Date dateFrom, Date dateTo,
                                                              FilterDocumentRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dateFrom != null && dateTo != null) {
                predicates.add(criteriaBuilder.between(root.get("createdDate"), dateFrom, dateTo));
            }
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if (request.getStatus() != null && !request.getStatus().equals("")) {
                try {
                    Document.Status status = Document.Status.valueOf(String.valueOf(request.getStatus()));
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                } catch (Exception e) {
                    throw new MyCustomException("Loại tài liệu tìm kiếm không hợp lệ!");
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
