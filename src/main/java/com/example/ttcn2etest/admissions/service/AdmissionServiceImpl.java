package com.example.ttcn2etest.admissions.service;

import com.example.ttcn2etest.admissions.dto.AdmissionDTO;
import com.example.ttcn2etest.admissions.entity.Admission;
import com.example.ttcn2etest.admissions.repository.AdmissionRepository;
import com.example.ttcn2etest.admissions.repository.CustomAdmissionRepository;
import com.example.ttcn2etest.admissions.request.CreateAdmissionRequest;
import com.example.ttcn2etest.admissions.request.FilterAdmissionRequest;
import com.example.ttcn2etest.admissions.request.UpdateAdmissionRequest;
import com.example.ttcn2etest.exception.MyCustomException;
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
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final ModelMapper modelMapper;

    public AdmissionServiceImpl(AdmissionRepository admissionRepository, ModelMapper modelMapper) {
        this.admissionRepository = admissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AdmissionDTO> getAllAdmissions() {
        return admissionRepository.findAll().stream().map(
                admission -> modelMapper.map(admission, AdmissionDTO.class)
        ).toList();
    }

    @Override
    public AdmissionDTO getByIdAdmission(Long id) {
        Optional<Admission> admissionOptional = admissionRepository.findById(id);
        if (admissionOptional.isPresent()) {
            return modelMapper.map(admissionOptional.get(), AdmissionDTO.class);
        } else {
            throw new MyCustomException("Id chương trình không tồn tại trong hệ thống!");
        }
    }

//    @Override
//    @Transactional
//    public AdmissionDTO createAdmission(CreateAdmissionRequest request) {
//        try {
//            Admission admission = Admission.builder()
//                    .title(request.getTitle())
//                    .description(request.getDescription())
//                    .admissionForm(request.getAdmissionForm())
//                    .image(request.getImage())
//                    .linkRegister(request.getLinkRegister())
//                    .createdDate(new Timestamp(System.currentTimeMillis()))
//                    .updateDate(new Timestamp(System.currentTimeMillis()))
//                    .build();
//            admission = admissionRepository.saveAndFlush(admission);
//            return modelMapper.map(admission, AdmissionDTO.class);
//        } catch (Exception ex) {
//            throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm chương trình mới!");
//        }
//    }
//
//    @Override
//    public AdmissionDTO updateAdmission(UpdateAdmissionRequest request, Long id) {
//        Optional<Admission> admissionOptional = admissionRepository.findById(id);
//        if (admissionOptional.isPresent()) {
//            Admission admission = admissionOptional.get();
//            admission.setTitle(request.getTitle());
//            admission.setDescription(request.getDescription());
//            admission.setAdmissionForm(request.getAdmissionForm());
//            admission.setImage(request.getImage());
//            admission.setLinkRegister(request.getLinkRegister());
//            admission.setUpdateDate(new Timestamp(System.currentTimeMillis()));
//            return modelMapper.map(admissionRepository.saveAndFlush(admission), AdmissionDTO.class);
//        }
//        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật chương trình!");
//    }

    @Override
    @Transactional
    public AdmissionDTO deleteByIdAdmission(Long id) {
        if (!admissionRepository.existsById(id)) {
            throw new MyCustomException("Chương trình có id: " + id + " cần xóa không tồn tại trong hệ thống!");
        }
        Optional<Admission> admissionOptional = admissionRepository.findById(id);
        if (admissionOptional.isPresent()) {
            admissionRepository.deleteById(id);
            return modelMapper.map(admissionOptional.get(), AdmissionDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa chương trình!");
    }

    @Override
    public List<AdmissionDTO> deleteAllAdmissions(List<Long> ids) {
        List<AdmissionDTO> admissionDTOS = new ArrayList<>();
        for (Long id : ids) {
            Optional<Admission> optionalAdmission = admissionRepository.findById(id);
            if (optionalAdmission.isPresent()) {
                Admission admission = optionalAdmission.get();
                admissionDTOS.add(modelMapper.map(admission, AdmissionDTO.class));
                admissionRepository.delete(admission);
            } else {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa danh sách chương trình!");
            }
        }
        return admissionDTOS;
    }

    @Override
    public Page<Admission> filterAdmissions(FilterAdmissionRequest request, Date dateFrom, Date dateTo) {
        Specification<Admission> specification = CustomAdmissionRepository.filterSpecification(dateFrom, dateTo, request);
        return admissionRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }
// trạng thái
        @Override
        @Transactional
        public AdmissionDTO createAdmission(CreateAdmissionRequest request) {
            try {
                Admission admission = Admission.builder()
                        .title(request.getTitle())
                        .program(request.getProgram())
                        .description(request.getDescription())
                        .admissionForm(request.getAdmissionForm())
                        .image(request.getImage())
                        .linkRegister(request.getLinkRegister())
                        .status(request.getStatus() != null ? request.getStatus() : true) // Mặc định là hiển thị
                        .createdDate(new Timestamp(System.currentTimeMillis()))
                        .updateDate(new Timestamp(System.currentTimeMillis()))
                        .build();
                admission = admissionRepository.saveAndFlush(admission);
                return modelMapper.map(admission, AdmissionDTO.class);
            } catch (Exception ex) {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm chương trình mới!");
            }
        }

    @Override
    public AdmissionDTO updateAdmission(UpdateAdmissionRequest request, Long id) {
        Optional<Admission> admissionOptional = admissionRepository.findById(id);
        if (admissionOptional.isPresent()) {
            Admission admission = admissionOptional.get();
            admission.setTitle(request.getTitle());
            admission.setProgram(request.getProgram());
            admission.setDescription(request.getDescription());
            admission.setAdmissionForm(request.getAdmissionForm());
            admission.setImage(request.getImage());
            admission.setLinkRegister(request.getLinkRegister());
            admission.setStatus(request.getStatus() != null ? request.getStatus() : admission.getStatus());
            admission.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(admissionRepository.saveAndFlush(admission), AdmissionDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật chương trình!");
    }



}