package com.alexcasey.spring_security_template.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alexcasey.spring_security_template.enums.RoleEnum;
import com.alexcasey.spring_security_template.model.Role;
import com.alexcasey.spring_security_template.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (!roleRepository.existsByRole(roleEnum)) {
                Role role = new Role(roleEnum);
                roleRepository.save(role);
                log.info("Inserted role: {}", roleEnum.name());
            } else {
                log.info("Role already exists: {}", roleEnum.name());
            }
        }
    }
}
