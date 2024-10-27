package com.alexcasey.spring_security_template.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexcasey.spring_security_template.enums.RoleEnum;
import com.alexcasey.spring_security_template.exception.RoleNotFoundException;
import com.alexcasey.spring_security_template.exception.UserAlreadyExistsException;
import com.alexcasey.spring_security_template.model.Account;
import com.alexcasey.spring_security_template.model.Role;
import com.alexcasey.spring_security_template.repository.AccountRepository;
import com.alexcasey.spring_security_template.repository.RoleRepository;
import com.alexcasey.spring_security_template.request.AuthRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public Account createAccount(AuthRequest request) {
        log.debug("Attempting to create account for username: {}", request.getUsername());

        Optional<Account> existingAccount = accountRepository.findByUsername(request.getUsername());
        if (existingAccount.isPresent()) {
            log.warn("Username already exists: {}", request.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        Account newAccount = new Account();
        newAccount.setUsername(request.getUsername());
        newAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByRole(RoleEnum.USER)
                .orElseThrow(() -> new RoleNotFoundException("User role not found"));
        newAccount.setRoles(Collections.singleton(userRole));

        Account savedAccount = accountRepository.save(newAccount);
        log.info("Successfully created account for username: {}", savedAccount.getUsername());
        return savedAccount;
    }
}
