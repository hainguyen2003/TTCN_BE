package com.example.ttcn2etest.service.classRoom;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.ClassDto;
import com.example.ttcn2etest.model.etity.Class;
import com.example.ttcn2etest.repository.classRoom.ClassRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ClassRoomServiceImpl implements ClassRoomService {
    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;

    public ClassRoomServiceImpl(ClassRepository classRepository, ModelMapper modelMapper) {
        this.classRepository = classRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<ClassDto> getAllClass() {
        return classRepository.findAll().stream().map(classRoom -> {
            try {
                return modelMapper.map(classRoom, ClassDto.class);
            } catch (Exception e) {
                System.out.println("Lỗi ánh xạ: " + e.getMessage());
                throw e;
            }
        }).toList();
    }

    @Override
    public ClassDto getClassById(String id) {
        Optional<Class> classRoom = classRepository.findById(id);
        if (classRoom.isPresent()) {
            return modelMapper.map(classRoom.get(), ClassDto.class);
        }else
        {
            throw new MyCustomException("ID của dịch vụ không tồn tại trong hệ thống!");
        }
    }

    @Override
    public ClassDto getClassesByCourseId(Long courseId) {
        Optional<Class> classes = classRepository.findByCourseId(courseId);
        if (classes.isPresent()) {
            return modelMapper.map(classes.get(), ClassDto.class);
        }else{
            throw new MyCustomException("ID của dịch vụ không tồn tại trong hệ thống!");
        }
    }


    @Override
    public ClassDto createClass(ClassDto classDto) {
        Class classEntity = modelMapper.map(classDto, Class.class);
        classEntity.setCreatedAt(Instant.now());
        classEntity.setUpdatedAt(Instant.now());
        classEntity = classRepository.save(classEntity);
        return modelMapper.map(classEntity, ClassDto.class);
    }

    @Override
    public ClassDto updateClass(String id, ClassDto classDto) {
        Class existingClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        // Cập nhật dữ liệu
        existingClass.setClassName(classDto.getClassName());
        existingClass.setTeacherName(classDto.getTeacherName());
        existingClass.setSchedule(classDto.getSchedule());
        existingClass.setStartDate(classDto.getStartDate());
        existingClass.setEndDate(classDto.getEndDate());
        existingClass.setUpdatedAt(Instant.now());

        existingClass = classRepository.save(existingClass);
        return modelMapper.map(existingClass, ClassDto.class);
    }

    @Override
    public void deleteClass(String id) {
        if (!classRepository.existsById(id)) {
            throw new RuntimeException("Class not found with id: " + id);
        }
        classRepository.deleteById(id);
    }
}
