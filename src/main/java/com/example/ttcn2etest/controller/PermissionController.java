package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.PermissionDTO;
import com.example.ttcn2etest.request.permission.CreatePermissionRequest;
import com.example.ttcn2etest.request.permission.UpdatePermissionRequest;
import com.example.ttcn2etest.service.permission.PermissionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    private final PermissionService permissionService;
    private final ModelMapper modelMapper;

    public PermissionController(PermissionService permissionService, ModelMapper modelMapper) {
        this.permissionService = permissionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllPermission() {
        try {
            List<PermissionDTO> response = permissionService.getAllPermission();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getByIdPermission(@PathVariable String id) {
        PermissionDTO response = permissionService.getByIdPermission(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    ResponseEntity<?> createPermission(@Validated @RequestBody CreatePermissionRequest request) {
        PermissionDTO response = permissionService.createPermission(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updatePermission(@Validated @RequestBody UpdatePermissionRequest request,
                                       @PathVariable("id") String id) {
        PermissionDTO response = permissionService.updatePermission(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteByIdPermission(@PathVariable String id) {
        PermissionDTO response = permissionService.deleteByIdPermission(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllIdPermission(@RequestBody List<String> ids) {
        try {
            List<PermissionDTO> response = permissionService.deleteAllIdPermission(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }
}
