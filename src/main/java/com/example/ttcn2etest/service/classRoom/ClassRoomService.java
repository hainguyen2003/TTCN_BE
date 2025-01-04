package com.example.ttcn2etest.service.classRoom;

import com.example.ttcn2etest.model.dto.ClassDto;

import java.util.List;

public interface ClassRoomService {
    List<ClassDto> getAllClass();

    ClassDto getClassById(String id);
    ClassDto getClassesByCourseId(Long courseId);

    ClassDto createClass(ClassDto classDto);

    ClassDto updateClass(String id, ClassDto classDto);

    void deleteClass(String id);

}
