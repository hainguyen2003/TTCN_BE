package com.example.ttcn2etest.service.permission;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.PermissionDTO;
import com.example.ttcn2etest.model.etity.Permission;
import com.example.ttcn2etest.repository.permission.PermissionRepository;
import com.example.ttcn2etest.request.permission.CreatePermissionRequest;
import com.example.ttcn2etest.request.permission.UpdatePermissionRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PermissionDTO> getAllPermission() {
        return permissionRepository.findAll().stream().map(
                permission -> modelMapper.map(permission, PermissionDTO.class)
        ).toList();
    }

    @Override
    public PermissionDTO getByIdPermission(String id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return modelMapper.map(permissionOptional.get(), PermissionDTO.class);
        } else {
            throw new MyCustomException("Id không tồn tại trong hệ thống!");
        }
    }

    @Override
    public PermissionDTO createPermission(CreatePermissionRequest request) {
        try {
            Permission permission = Permission.builder()
                    .permissionId(request.getPermissionId())
                    .name(request.getName())
                    .description(request.getDescription())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            permission = permissionRepository.saveAndFlush(permission);
            return modelMapper.map(permission, PermissionDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm mới!");
        }
    }

    @Override
    public PermissionDTO updatePermission(UpdatePermissionRequest request, String id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            Permission permission = permissionOptional.get();
            permission.setName(request.getName());
            permission.setDescription(request.getDescription());
            permission.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(permissionRepository.saveAndFlush(permission), PermissionDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật quyền!");
    }

    @Override
    @Transactional
    public PermissionDTO deleteByIdPermission(String id) {
        if (!permissionRepository.existsById(id)) {
            throw new MyCustomException("Quyền có id: " + id + "cần xóa không tồn tại trong hệ thống!");
        }
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            permissionRepository.deleteById(id);
            return modelMapper.map(permissionOptional, PermissionDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trinh xóa!");
    }

    @Override
    public List<PermissionDTO> deleteAllIdPermission(List<String> ids) {
        List<PermissionDTO> permissionDTOS = new ArrayList<>();
        for (String id : ids) {
            Optional<Permission> permissionOptional = permissionRepository.findById(id);
            if (permissionOptional.isPresent()) {
                Permission permission = permissionOptional.get();
                permissionDTOS.add(modelMapper.map(permission, PermissionDTO.class));
                permissionRepository.delete(permission);
            } else {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa!");
            }
        }
        return permissionDTOS;
    }


}
