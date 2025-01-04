package com.example.ttcn2etest.repository.displayManager;

import com.example.ttcn2etest.model.etity.DisplayManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayManagerRepository extends JpaRepository<DisplayManager, Long>, JpaSpecificationExecutor<DisplayManager> {
}
