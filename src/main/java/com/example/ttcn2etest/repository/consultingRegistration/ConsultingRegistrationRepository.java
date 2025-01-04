package com.example.ttcn2etest.repository.consultingRegistration;

import com.example.ttcn2etest.model.etity.ConsultingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultingRegistrationRepository extends JpaRepository<ConsultingRegistration, Long>, JpaSpecificationExecutor<ConsultingRegistration> {
}
