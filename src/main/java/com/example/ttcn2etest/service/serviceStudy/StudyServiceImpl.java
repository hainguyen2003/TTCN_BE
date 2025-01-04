package com.example.ttcn2etest.service.serviceStudy;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.ServiceDTO;
import com.example.ttcn2etest.repository.service.CustomServiceRepository;
import com.example.ttcn2etest.repository.service.ServiceRepository;
import com.example.ttcn2etest.request.service.FilterServiceRequest;
import com.example.ttcn2etest.request.service.ServiceRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudyServiceImpl implements StudyService {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    public StudyServiceImpl(ServiceRepository serviceRepository, ModelMapper modelMapper) {
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ServiceDTO> getAllService() {
        return serviceRepository.findAll().stream().map(
                service -> modelMapper.map(service, ServiceDTO.class)
        ).toList();
    }

    @Override
    public ServiceDTO getByIdService(Long id) {
        Optional<com.example.ttcn2etest.model.etity.Service> service = serviceRepository.findById(id);
        if (service.isPresent()) {
            return modelMapper.map(service.get(), ServiceDTO.class);
        } else {
            throw new MyCustomException("ID của dịch vụ không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public ServiceDTO createService(ServiceRequest request) {
        try {
            com.example.ttcn2etest.model.etity.Service service = com.example.ttcn2etest.model.etity.Service.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .detailDescription(request.getDetailDescription())
                    .studyGoals(request.getStudyGoals())
                    .schedule(request.getSchedule())
                    .numberTeachingSessions(request.getNumberTeachingSessions())
                    .curriculum(request.getCurriculum())
                    .learnOnlineOrOffline(request.getLearnOnlineOrOffline())
                    .learningForm(request.getLearningForm())
                    .coursePrice(request.getCoursePrice())
                    .price(request.getPrice())
                    .requestStudents(request.getRequestStudents())
                    .typeOfService(request.getTypeOfService())
                    .image(request.getImage())
                    .content(request.getContent())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            service = serviceRepository.saveAndFlush(service);
            return modelMapper.map(service, ServiceDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Có lỗi xảy ra trong quá trình tạo dịch vụ học!");
        }
    }

    @Override
    @Transactional
    public ServiceDTO updateService(ServiceRequest request, Long id) {
        Optional<com.example.ttcn2etest.model.etity.Service> serviceOptional = serviceRepository.findById(id);
        if (serviceOptional.isPresent()) {
            com.example.ttcn2etest.model.etity.Service service = serviceOptional.get();
            service.setName(request.getName());
            service.setDescription(request.getDescription());
            service.setDetailDescription(request.getDetailDescription());
            service.setStudyGoals(request.getStudyGoals());
            service.setSchedule(request.getSchedule());
            service.setNumberTeachingSessions(request.getNumberTeachingSessions());
            service.setCurriculum(request.getCurriculum());
            service.setLearnOnlineOrOffline(request.getLearnOnlineOrOffline());
            service.setLearningForm(request.getLearningForm());
            service.setCoursePrice(request.getCoursePrice());
            service.setPrice(request.getPrice());
            service.setTypeOfService(request.getTypeOfService());
            service.setImage(request.getImage());
            service.setContent(request.getContent());
            service.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(serviceRepository.save(service), ServiceDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật dịch vụ!");
    }

    @Override
    @Transactional
    public ServiceDTO deleteByIdService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Dịch vụ có id: " + id + "cần xóa không tồn tại trong hệ thống!");
        }
        Optional<com.example.ttcn2etest.model.etity.Service> serviceOptional = serviceRepository.findById(id);
        if (serviceOptional.isPresent()) {
            serviceRepository.deleteById(id);
            return modelMapper.map(serviceOptional, ServiceDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa dịch vụ!");
    }

    @Override
    public List<ServiceDTO> deleteAllIdService(List<Long> ids) {
        List<ServiceDTO> serviceDTOS = new ArrayList<>();
        for (com.example.ttcn2etest.model.etity.Service service : serviceRepository.findAllById(ids)) {
            serviceDTOS.add(modelMapper.map(service, ServiceDTO.class));
        }
        serviceRepository.deleteAllByIdInBatch(ids);
        return serviceDTOS;
    }

    @Override
    public Page<com.example.ttcn2etest.model.etity.Service> filterService(FilterServiceRequest request, Date dateFrom, Date dateTo, BigDecimal maxPrice, BigDecimal minPrice) {
        Specification<com.example.ttcn2etest.model.etity.Service> specification = CustomServiceRepository.filterSpecification(dateFrom, dateTo, maxPrice, minPrice, request);
        return serviceRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }
}
