package com.example.ttcn2etest.service.serviceStudy;

import com.example.ttcn2etest.model.dto.ServiceDTO;
import com.example.ttcn2etest.model.etity.Service;
import com.example.ttcn2etest.request.service.FilterServiceRequest;
import com.example.ttcn2etest.request.service.ServiceRequest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface StudyService {
    List<ServiceDTO> getAllService();

    ServiceDTO getByIdService(Long id);

    ServiceDTO createService(ServiceRequest request);

    ServiceDTO updateService(ServiceRequest request, Long id);

    ServiceDTO deleteByIdService(Long id);

    List<ServiceDTO> deleteAllIdService(List<Long> ids);

    Page<Service> filterService(FilterServiceRequest request, Date dateFrom, Date dateTo, BigDecimal maxPrice, BigDecimal minPrice);

}
