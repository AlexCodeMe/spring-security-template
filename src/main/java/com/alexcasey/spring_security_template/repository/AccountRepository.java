package com.alexcasey.spring_security_template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexcasey.spring_security_template.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
