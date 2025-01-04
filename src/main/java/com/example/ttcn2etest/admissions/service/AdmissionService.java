package com.example.ttcn2etest.admissions.service;


import com.example.ttcn2etest.admissions.dto.AdmissionDTO;
import com.example.ttcn2etest.admissions.entity.Admission;
import com.example.ttcn2etest.admissions.request.CreateAdmissionRequest;
import com.example.ttcn2etest.admissions.request.FilterAdmissionRequest;
import com.example.ttcn2etest.admissions.request.UpdateAdmissionRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;


public interface AdmissionService {
    List<AdmissionDTO> getAllAdmissions();

    AdmissionDTO getByIdAdmission(Long id);

    AdmissionDTO createAdmission(CreateAdmissionRequest request);

    AdmissionDTO updateAdmission(UpdateAdmissionRequest request, Long id);

    AdmissionDTO deleteByIdAdmission(Long id);

    List<AdmissionDTO> deleteAllAdmissions(List<Long> ids);

    Page<Admission> filterAdmissions(FilterAdmissionRequest request, Date dateFrom, Date dateTo);
}