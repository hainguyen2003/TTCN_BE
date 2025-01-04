package com.example.ttcn2etest.repository.user;

import com.example.ttcn2etest.model.etity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(String userName);

    Optional<User> findByUsername(String username);

    User getUserByUsername(String username);

    boolean existsAllByUsername(String username);

    boolean existsAllByUsernameAndIdNot(String username, Long userId);

    boolean existsAllByEmail(String email);

    boolean existsAllByPhone(String phone);

    List<User> findAllByIsSuperAdminFalse();

    User findByEmail(String email);
}
