package com.example.ttcn2etest.service.role;

import com.example.ttcn2etest.model.dto.RoleDTO;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.request.role.CreateRoleRequest;
import com.example.ttcn2etest.request.role.FilterRoleRequest;
import com.example.ttcn2etest.request.role.UpdateRoleRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRole();

    RoleDTO getByIdRole(String id);

    RoleDTO createRole(CreateRoleRequest request);

    RoleDTO updateRole(UpdateRoleRequest request, String id);

    RoleDTO deleteByIdRole(String id);

    List<RoleDTO> deleteAllIdRole(List<String> ids);

    Page<Role> filterRole(FilterRoleRequest request, Date dateFrom, Date dateTo);
}
