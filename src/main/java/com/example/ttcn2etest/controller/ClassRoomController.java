package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.ClassDto;
import com.example.ttcn2etest.service.classRoom.ClassRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classRoom")
@CrossOrigin(origins = "http://localhost:3000")
public class ClassRoomController extends BaseController {
    private final ClassRoomService classRoomService;
    private final ModelMapper modelMapper;

    public ClassRoomController(ClassRoomService classRoomService, ModelMapper modelMapper) {
        this.classRoomService = classRoomService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllClass(){
        try {
            List<ClassDto> response = classRoomService.getAllClass();
            return buildListItemResponse(response, response.size());
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Chi tiết lỗi: " + e.getMessage());
        }
    }
    // API: Lấy thông tin lớp học theo ID
    @GetMapping("/{id}")
    ResponseEntity<?> getClassById(@PathVariable String id) {
        try {
            ClassDto response = classRoomService.getClassById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy lớp học với id: " + id + ". Chi tiết lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/by-course/{courseId}")
    ResponseEntity<?> getClassesByCourseId(@PathVariable Long courseId) {
        ClassDto response = classRoomService.getClassesByCourseId(courseId);
        return buildItemResponse(response);
    }

    // API: Tạo lớp học mới
    @PostMapping("/create")
    ResponseEntity<?> createClass(@RequestBody ClassDto classDto) {
        try {
            ClassDto response = classRoomService.createClass(classDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể tạo lớp học. Chi tiết lỗi: " + e.getMessage());
        }
    }

    // API: Cập nhật lớp học
    @PutMapping("/update/{id}")
    ResponseEntity<?> updateClass(@PathVariable String id, @RequestBody ClassDto classDto) {
        try {
            ClassDto response = classRoomService.updateClass(id, classDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể cập nhật lớp học với id: " + id + ". Chi tiết lỗi: " + e.getMessage());
        }
    }

    // API: Xóa lớp học
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteClass(@PathVariable String id) {
        try {
            classRoomService.deleteClass(id);
            return ResponseEntity.ok("Xóa lớp học thành công với id: " + id);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không thể xóa lớp học với id: " + id + ". Chi tiết lỗi: " + e.getMessage());
        }
    }

}
