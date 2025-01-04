package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.dto.RoleDTO;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.request.role.CreateRoleRequest;
import com.example.ttcn2etest.request.role.FilterRoleRequest;
import com.example.ttcn2etest.request.role.UpdateRoleRequest;
import com.example.ttcn2etest.service.role.RoleService;
import com.example.ttcn2etest.utils.MyUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController {
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public RoleController(RoleService roleService, ModelMapper modelMapper) {
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllRole() {
        try {
            List<RoleDTO> response = roleService.getAllRole();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getByIdRole(@PathVariable String id) {
        RoleDTO response = roleService.getByIdRole(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    ResponseEntity<?> createRole(@Validated @RequestBody CreateRoleRequest request) {
        RoleDTO response = roleService.createRole(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateRole(@Validated @RequestBody UpdateRoleRequest request,
                                 @PathVariable("id") String id) {
        RoleDTO response = roleService.updateRole(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteByIdRole(@PathVariable String id) {
        RoleDTO response = roleService.deleteByIdRole(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllIdRole(@RequestBody List<String> ids) {
        try {
            List<RoleDTO> response = roleService.deleteAllIdRole(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterRole(@Validated @RequestBody FilterRoleRequest request) throws ParseException {
        Page<Role> rolePage = roleService.filterRole(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null);
        List<RoleDTO> roleDTOS = rolePage.getContent().stream().map(
                role -> modelMapper.map(role, RoleDTO.class)
        ).toList();
        return buildListItemResponse(roleDTOS, rolePage.getTotalElements());
    }
}
