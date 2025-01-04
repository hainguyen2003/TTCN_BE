package com.example.ttcn2etest.admissions.controller;


import com.example.ttcn2etest.admissions.dto.AdmissionDTO;
import com.example.ttcn2etest.admissions.entity.Admission;
import com.example.ttcn2etest.admissions.repository.AdmissionRepository;
import com.example.ttcn2etest.admissions.request.CreateAdmissionRequest;
import com.example.ttcn2etest.admissions.request.FilterAdmissionRequest;
import com.example.ttcn2etest.admissions.request.UpdateAdmissionRequest;
import com.example.ttcn2etest.admissions.service.AdmissionService;
import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.controller.BaseController;
import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.utils.MyUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admissions")
public class AdmissionController extends BaseController {

    private final AdmissionService admissionService;
    private final AdmissionRepository admissionRepository; // Thêm dependency
    private final ModelMapper modelMapper;

    public AdmissionController(AdmissionService admissionService, AdmissionRepository admissionRepository, ModelMapper modelMapper) {
        this.admissionService = admissionService;
        this.admissionRepository = admissionRepository; // Inject dependency đúng cách
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllAdmissions() {
        try {
            List<AdmissionDTO> response = admissionService.getAllAdmissions();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        AdmissionDTO response = admissionService.getByIdAdmission(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> createAdmission(@Validated @RequestBody CreateAdmissionRequest request) {
        AdmissionDTO response = admissionService.createAdmission(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> updateAdmission(@Validated @RequestBody UpdateAdmissionRequest request,
                                      @PathVariable("id") Long id) {
        AdmissionDTO response = admissionService.updateAdmission(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        admissionService.deleteByIdAdmission(id);
        return buildResponse();
    }

    @DeleteMapping("/delete/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteAllById(@RequestBody List<Long> ids) {
        try {
            admissionService.deleteAllAdmissions(ids);
            return buildResponse();
        } catch (Exception ex) {
            return buildResponse();
        }
    }



    @PostMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> filter(@Validated @RequestBody FilterAdmissionRequest request) throws ParseException {
        // Giả sử có service tương tự cho Admission
        Page<Admission> admissionPage = admissionService.filterAdmissions(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null
        );

        // Chuyển đổi từ entity Admission sang DTO
        List<AdmissionDTO> admissionDTOS = admissionPage.getContent().stream().map(
                admission -> modelMapper.map(admission, AdmissionDTO.class)
        ).toList();

        // Trả về phản hồi theo dạng list item response với tổng số lượng
        return buildListItemResponse(admissionDTOS, admissionPage.getTotalElements());
    }


    //trạng thái

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Boolean status) {
        Optional<Admission> admissionOptional = admissionRepository.findById(id);
        if (admissionOptional.isPresent()) {
            Admission admission = admissionOptional.get();
            admission.setStatus(status);
            admission.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            admissionRepository.save(admission);
           // return buildResponse("Status updated successfully!");
            return buildItemResponse("Status updated successfully!");
        }
        throw new MyCustomException("Chương trình không tồn tại!");
    }



}