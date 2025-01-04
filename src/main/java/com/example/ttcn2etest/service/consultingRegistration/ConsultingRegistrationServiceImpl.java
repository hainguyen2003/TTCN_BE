package com.example.ttcn2etest.service.consultingRegistration;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.ConsultingRegistrationDTO;
import com.example.ttcn2etest.model.etity.ConsultingRegistration;
import com.example.ttcn2etest.repository.consultingRegistration.ConsultingRegistrationRepository;
import com.example.ttcn2etest.repository.consultingRegistration.CustomConsultingRegistrationRepository;
import com.example.ttcn2etest.request.consultingRegistration.CreateConsultingRegistrationRequest;
import com.example.ttcn2etest.request.consultingRegistration.FilterConsultingRegistrationRequest;
import com.example.ttcn2etest.request.consultingRegistration.UpdateConsultingRegistrationRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultingRegistrationServiceImpl implements ConsultingRegistrationService {
    private final ConsultingRegistrationRepository consultingRegistrationRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public ConsultingRegistrationServiceImpl(ConsultingRegistrationRepository consultingRegistrationRepository) {
        this.consultingRegistrationRepository = consultingRegistrationRepository;
    }

    @Override
    public List<ConsultingRegistrationDTO> getAllConsultingRegistration() {
        return consultingRegistrationRepository.findAll().stream().map(
                consultingRegistration -> modelMapper.map(consultingRegistration, ConsultingRegistrationDTO.class)
        ).toList();
    }

    @Override
    public ConsultingRegistrationDTO getByIdConsultingRegistration(Long id) {
        Optional<ConsultingRegistration> consultingRegistration = consultingRegistrationRepository.findById(id);
        if (consultingRegistration.isPresent()) {
            return modelMapper.map(consultingRegistration.get(), ConsultingRegistrationDTO.class);
        } else {
            throw new MyCustomException("ID không tồn tại trong hệ thống!");
        }
    }

    @Override
    public ConsultingRegistrationDTO createConsultingRegistration(CreateConsultingRegistrationRequest request) {
        try {
            ConsultingRegistration consultingRegistration = ConsultingRegistration.builder()
                    .name(request.getName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .contentAdvice(request.getContentAdvice())
                    .status(request.getStatus())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            consultingRegistration = consultingRegistrationRepository.saveAndFlush(consultingRegistration);
            return modelMapper.map(consultingRegistration, ConsultingRegistrationDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm  mới!");
        }
    }

    @Override
    @Transactional
    public ConsultingRegistrationDTO updateConsultingRegistration(UpdateConsultingRegistrationRequest request, Long id) {
        Optional<ConsultingRegistration> consultingRegistrationOptional = consultingRegistrationRepository.findById(id);
        if (consultingRegistrationOptional.isPresent()) {
            ConsultingRegistration consultingRegistration = consultingRegistrationOptional.get();
            consultingRegistration.setName(request.getName());
            consultingRegistration.setPhone(request.getPhone());
            consultingRegistration.setEmail(request.getEmail());
            consultingRegistration.setStatus(request.getStatus());
            consultingRegistration.setContentAdvice(request.getContentAdvice());
            consultingRegistration.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(consultingRegistration, ConsultingRegistrationDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật!");
    }

    @Override
    @Transactional
    public ConsultingRegistrationDTO deleteByIdConsultingRegistration(Long id) {
        if (!consultingRegistrationRepository.existsById(id)) {
            throw new MyCustomException("Id: " + id + " cần xóa không tồn tại trong hệ thống!");
        }
        Optional<ConsultingRegistration> consultingRegistrationOptional = consultingRegistrationRepository.findById(id);
        if (consultingRegistrationOptional.isPresent()) {
            consultingRegistrationRepository.deleteById(id);
            return modelMapper.map(consultingRegistrationOptional, ConsultingRegistrationDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trinh xóa!");
    }

    @Override
    public List<ConsultingRegistrationDTO> deleteAllConsultingRegistration(List<Long> ids) {
        List<ConsultingRegistrationDTO> consultingRegistrationDTOS = new ArrayList<>();
        for (Long id : ids) {
            Optional<ConsultingRegistration> optionalConsultingRegistration = consultingRegistrationRepository.findById(id);
            if (optionalConsultingRegistration.isPresent()) {
                ConsultingRegistration consultingRegistration = optionalConsultingRegistration.get();
                consultingRegistrationDTOS.add(modelMapper.map(consultingRegistration, ConsultingRegistrationDTO.class));
                consultingRegistrationRepository.delete(consultingRegistration);
            } else {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa danh sách đăng ký tư vấn!");
            }

        }
        return consultingRegistrationDTOS;
    }

    @Override
    public Page<ConsultingRegistration> filterConsultingRegistration(FilterConsultingRegistrationRequest request, Date dateFrom, Date dateTo) {
        Specification<ConsultingRegistration> specification = CustomConsultingRegistrationRepository.filterSpecification(dateFrom, dateTo, request);
        Page<ConsultingRegistration> consultingRegistrationPage = consultingRegistrationRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return consultingRegistrationPage;
    }
}
