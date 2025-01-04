package com.example.ttcn2etest.repository.slide;

import com.example.ttcn2etest.model.etity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long>, JpaSpecificationExecutor<Slide> {
}
