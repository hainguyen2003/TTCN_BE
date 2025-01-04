package com.example.ttcn2etest.service.classUser;

import com.example.ttcn2etest.model.dto.ClassUserDto;

import java.util.List;

public interface ClassUserService {
    List<ClassUserDto> getAllClassUser();
    ClassUserDto getClassById(String id);
    ClassUserDto createClassUser(ClassUserDto clasUserDto);
    ClassUserDto updateClassUser(String id, ClassUserDto clasUserDto);
    void deleteClassUser(Long id);
}
