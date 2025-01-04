package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.ClassUserDto;
import com.example.ttcn2etest.service.classUser.ClassUserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class-user")
@CrossOrigin(origins = "http://localhost:3000")
public class ClassUserController extends BaseController {
    private final ClassUserService classUserService;
    private final ModelMapper modelMapper;

    public ClassUserController(ClassUserService classUserService, ModelMapper modelMapper) {
        this.classUserService = classUserService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllClassUser() {
        try {
            List<ClassUserDto> response = classUserService.getAllClassUser();
            return buildListItemResponse(response, response.size());
        }catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Chi tiết lỗi: " + e.getMessage());
        }
    }

    // API: Lấy thông tin lớp học theo ID lớp học
    @GetMapping("/{id}")
    ResponseEntity<?> getClassById(@PathVariable String id) {
        try {
            ClassUserDto response = classUserService.getClassById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy lớp học với id: " + id + ". Chi tiết lỗi: " + e.getMessage());
        }
    }

    // API: Tạo lớp học mới
    @PostMapping("/create")
    ResponseEntity<?> createClass(@RequestBody ClassUserDto classUserDto) {
        try {
            ClassUserDto response = classUserService.createClassUser(classUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể tạo lớp học. Chi tiết lỗi: " + e.getMessage());
        }
    }

    // API: Xóa lớp học
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteClass(@PathVariable Long id) {
        try {
            classUserService.deleteClassUser(id);
            return ResponseEntity.ok("Xóa lớp học thành công với id: " + id);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không thể xóa lớp học với id: " + id + ". Chi tiết lỗi: " + e.getMessage());
        }
    }
}
