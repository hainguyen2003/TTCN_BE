package com.example.ttcn2etest.service.role;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.RoleDTO;
import com.example.ttcn2etest.model.etity.Permission;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.repository.permission.PermissionRepository;
import com.example.ttcn2etest.repository.role.CustomRoleRepository;
import com.example.ttcn2etest.repository.role.RoleRepository;
import com.example.ttcn2etest.request.role.CreateRoleRequest;
import com.example.ttcn2etest.request.role.FilterRoleRequest;
import com.example.ttcn2etest.request.role.UpdateRoleRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RoleDTO> getAllRole() {
        return roleRepository.findAll().stream().map(
                role -> modelMapper.map(role, RoleDTO.class)
        ).toList();
    }

    @Override
    public RoleDTO getByIdRole(String id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return modelMapper.map(roleOptional.get(), RoleDTO.class);
        } else {
            throw new MyCustomException("Id quyền không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public RoleDTO createRole(CreateRoleRequest request) {
        try {
            checkPermissionIsValid(request.getPermissionIds());
            Role role = Role.builder()
                    .roleId(request.getRoleId())
                    .name(request.getName())
                    .description(request.getDescription())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            List<Permission> permissions = buildPermission(request.getPermissionIds());
            role.setPermissions(permissions);

            role = roleRepository.saveAndFlush(role);
            return modelMapper.map(role, RoleDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm vai trò mới!");
        }
    }

    @Override
    public RoleDTO updateRole(UpdateRoleRequest request, String id) {
        validateRoleExist(id);
        Optional<Role> roleOptional = roleRepository.findById(id);
        checkPermissionIsValid(request.getPermissionIds());
        if (roleOptional.isPresent()) {
            List<Permission> permissions = buildPermission(request.getPermissionIds());
            Role role = roleOptional.get();
            role.setName(request.getName());
            role.setDescription(request.getDescription());
            role.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            role.setPermissions(permissions);
            return modelMapper.map(roleRepository.saveAndFlush(role), RoleDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật quyền!");
    }

    @Override
    @Transactional
    public RoleDTO deleteByIdRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new MyCustomException("Quyền có id: " + id + "cần xóa không tồn tại trong hệ thống!");
        }
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            roleRepository.deleteById(id);
            return modelMapper.map(roleOptional, RoleDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa quyền!");
    }

    @Override
    public List<RoleDTO> deleteAllIdRole(List<String> ids) {
        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (String id : ids) {
            Optional<Role> optionalRole = roleRepository.findById(id);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                roleDTOS.add(modelMapper.map(role, RoleDTO.class));
                roleRepository.delete(role);
            } else {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa!");
            }
        }
        return roleDTOS;
    }

    @Override
    public Page<Role> filterRole(FilterRoleRequest request, Date dateFrom, Date dateTo) {
        Specification<Role> specification = CustomRoleRepository.filterSpecification(dateFrom, dateTo, request);
        return roleRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }

    private void checkPermissionIsValid(List<String> permissionIds) {
        List<Permission> permissions = buildPermission(permissionIds);
        if (CollectionUtils.isEmpty(permissions)) {
            throw new MyCustomException("Permission không tồn tại");
        }
        List<String> listIdExists = permissions.stream().map(Permission::getPermissionId).toList();
        List<String> idNotExists = permissionIds.stream().filter(s -> !listIdExists.contains(s)).toList();
        if (!idNotExists.isEmpty())
            throw new MyCustomException(String.format("Trong danh sách permision ids có mã không tồn tại trên hệ thống: %s!", idNotExists));
    }

    private List<Permission> buildPermission(List<String> permissionIds) {
        return permissionRepository.findAllById(permissionIds);
    }

    private void validateRoleExist(String id) {
        boolean isExist = roleRepository.existsById(id);
        if (!isExist)
            throw new MyCustomException("Vai trò không tồn tại trên hệ thống");
    }
}
