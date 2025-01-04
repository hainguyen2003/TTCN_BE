package com.example.ttcn2etest.repository.classUser;

import com.example.ttcn2etest.model.etity.ClassUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassUserRepository extends JpaRepository<ClassUser, Long> {
    @Query(value = "SELECT * FROM class_user WHERE class_id = :classId ", nativeQuery = true)
    Optional<ClassUser> findAllClassByClassId(@Param("classId") String classId);
}