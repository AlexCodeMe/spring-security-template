package com.alexcasey.spring_security_template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexcasey.spring_security_template.enums.RoleEnum;
import com.alexcasey.spring_security_template.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum role);
    boolean existsByRole(RoleEnum role);
}
