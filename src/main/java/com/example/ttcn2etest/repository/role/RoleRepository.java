package com.example.ttcn2etest.repository.role;

import com.example.ttcn2etest.model.etity.Permission;
import com.example.ttcn2etest.model.etity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Role findByRoleId(String roleId);
}
