package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.dto.SlideDTO;
import com.example.ttcn2etest.model.etity.Slide;
import com.example.ttcn2etest.request.slide.CreateSlideRequest;
import com.example.ttcn2etest.request.slide.FilterSlideRequest;
import com.example.ttcn2etest.request.slide.UpdateSlideRequest;
import com.example.ttcn2etest.service.slide.SlideService;
import com.example.ttcn2etest.utils.MyUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/slide")
public class SlideController extends BaseController {
    private final SlideService slideService;
    private final ModelMapper modelMapper;

    public SlideController(SlideService slideService, ModelMapper modelMapper) {
        this.slideService = slideService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllSlide() {
        try {
            List<SlideDTO> response = slideService.getAllSlide();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable("id") Long id) {
        SlideDTO response = slideService.getById(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    ResponseEntity<?> createSlide(@Validated @RequestBody CreateSlideRequest request) {
        SlideDTO response = slideService.createSlide(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> update(@Validated @RequestBody UpdateSlideRequest request,
                             @PathVariable("id") Long id) {
        SlideDTO response = slideService.update(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        SlideDTO response = slideService.deleteByIdService(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        try {
            List<SlideDTO> response = slideService.deleteAllId(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> filter(@Validated @RequestBody FilterSlideRequest request) throws ParseException {
        Page<Slide> slidePage = slideService.filterService(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null
        );
        List<SlideDTO> slideDTOS = slidePage.getContent().stream().map(
                slide -> modelMapper.map(slide, SlideDTO.class)
        ).toList();
        return buildListItemResponse(slideDTOS, slidePage.getTotalElements());
    }

}
