package com.alexcasey.spring_security_template.repository;

import java.util.Optional;

import com.alexcasey.spring_security_template.enums.RoleEnum;
import com.alexcasey.spring_security_template.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum role);
}
