package com.example.ttcn2etest.service.permission;

import com.example.ttcn2etest.model.dto.PermissionDTO;
import com.example.ttcn2etest.request.permission.CreatePermissionRequest;
import com.example.ttcn2etest.request.permission.UpdatePermissionRequest;

import java.util.List;

public interface PermissionService {
    List<PermissionDTO> getAllPermission();

    PermissionDTO getByIdPermission(String id);

    PermissionDTO createPermission(CreatePermissionRequest request);

    PermissionDTO updatePermission(UpdatePermissionRequest request, String id);

    PermissionDTO deleteByIdPermission(String id);

    List<PermissionDTO> deleteAllIdPermission(List<String> ids);
}
