package com.example.ttcn2etest.admissions.repository;


import com.example.ttcn2etest.admissions.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long>, JpaSpecificationExecutor<Admission> {
}


