package com.example.ttcn2etest.service.consultingRegistration;

import com.example.ttcn2etest.model.dto.ConsultingRegistrationDTO;
import com.example.ttcn2etest.model.etity.ConsultingRegistration;
import com.example.ttcn2etest.request.consultingRegistration.CreateConsultingRegistrationRequest;
import com.example.ttcn2etest.request.consultingRegistration.FilterConsultingRegistrationRequest;
import com.example.ttcn2etest.request.consultingRegistration.UpdateConsultingRegistrationRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface ConsultingRegistrationService {
    List<ConsultingRegistrationDTO> getAllConsultingRegistration();

    ConsultingRegistrationDTO getByIdConsultingRegistration(Long id);

    ConsultingRegistrationDTO createConsultingRegistration(CreateConsultingRegistrationRequest request);

    ConsultingRegistrationDTO updateConsultingRegistration(UpdateConsultingRegistrationRequest request, Long id);

    ConsultingRegistrationDTO deleteByIdConsultingRegistration(Long id);

    List<ConsultingRegistrationDTO> deleteAllConsultingRegistration(List<Long> ids);

    Page<ConsultingRegistration> filterConsultingRegistration(FilterConsultingRegistrationRequest request, Date dateFrom, Date dateTo);
}
