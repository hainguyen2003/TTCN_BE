package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.dto.DisplayManagerDTO;
import com.example.ttcn2etest.model.etity.DisplayManager;
import com.example.ttcn2etest.request.displayManager.CreateDisplayRequest;
import com.example.ttcn2etest.request.displayManager.FilterDisplayRequest;
import com.example.ttcn2etest.request.displayManager.UpdateDisplayRequest;
import com.example.ttcn2etest.service.displayManager.DisplayService;
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
@RequestMapping("/display")
public class DisplayManagerController extends BaseController {
    private final DisplayService displayService;
    private final ModelMapper modelMapper;

    public DisplayManagerController(DisplayService displayService, ModelMapper modelMapper) {
        this.displayService = displayService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllDisplayManager() {
        try {
            List<DisplayManagerDTO> response = displayService.getAllDisplayManager();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        DisplayManagerDTO response = displayService.getByIdDisplay(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> createDocument(@Validated @RequestBody CreateDisplayRequest request) {
        DisplayManagerDTO response = displayService.createDisplay(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> updateService(@Validated @RequestBody UpdateDisplayRequest request,
                                    @PathVariable("id") Long id) {
        DisplayManagerDTO response = displayService.updateDisplay(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        DisplayManagerDTO response = displayService.deleteByIdDisplay(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all1")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        try {
            List<DisplayManagerDTO> response = displayService.deleteAllDisplay(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/delete/all2")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteAllIdDocument(@RequestBody List<Long> ids) {
        try {
            List<DisplayManagerDTO> response = displayService.deleteAllDisplayManager(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> filter(@Validated @RequestBody FilterDisplayRequest request) throws ParseException {
        Page<DisplayManager> displayPage = displayService.filterDisplay(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null);
        List<DisplayManagerDTO> displayManagerDTOS = displayPage.getContent().stream().map(
                displayManager -> modelMapper.map(displayManager, DisplayManagerDTO.class)
        ).toList();
        return buildListItemResponse(displayManagerDTOS, displayPage.getTotalElements());
    }


}
