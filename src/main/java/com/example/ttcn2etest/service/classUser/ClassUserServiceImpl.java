package com.example.ttcn2etest.service.classUser;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.ClassUserDto;
import com.example.ttcn2etest.model.etity.ClassUser;
import com.example.ttcn2etest.repository.classUser.ClassUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassUserServiceImpl implements ClassUserService{
    private final ModelMapper modelMapper;
    private final ClassUserRepository classUserRepository;

    public ClassUserServiceImpl(ModelMapper modelMapper, ClassUserRepository classUserRepository) {
        this.modelMapper = modelMapper;
        this.classUserRepository = classUserRepository;
    }

    @Override
    public List<ClassUserDto> getAllClassUser() {
        return classUserRepository.findAll().stream().map(classUser -> {
            try {
                return modelMapper.map(classUser, ClassUserDto.class);
            } catch (Exception e) {
                System.out.println("Lỗi ánh xạ: " + e.getMessage());
                throw e;
            }
        }).toList();
    }

    @Override
    public ClassUserDto getClassById(String id) {
        Optional<ClassUser> classUser = classUserRepository.findAllClassByClassId(id);
        if (classUser.isPresent()) {
            return modelMapper.map(classUser.get(), ClassUserDto.class);
        }else{
             throw new MyCustomException("ID của dịch vụ không tồn tại");
        }
    }

    @Override
    public ClassUserDto createClassUser(ClassUserDto clasUserDto) {
        ClassUser classUser = modelMapper.map(clasUserDto, ClassUser.class);
        classUser = classUserRepository.save(classUser);
        return modelMapper.map(classUser,ClassUserDto.class);
    }

    @Override
    public ClassUserDto updateClassUser(String id, ClassUserDto clasUserDto) {
        return null;
    }


    @Override
    public void deleteClassUser(Long id) {
        if (!classUserRepository.existsById(id)){
            throw new RuntimeException("Class user not found with id: " + id);
        }
        classUserRepository.deleteById(id);
    }
}
