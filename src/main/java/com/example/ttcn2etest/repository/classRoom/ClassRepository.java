package com.example.ttcn2etest.repository.classRoom;

import com.example.ttcn2etest.model.etity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, String> {
    Optional<Class> findByCourseId(Long courseId);
}