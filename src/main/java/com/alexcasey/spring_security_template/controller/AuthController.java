package com.alexcasey.spring_security_template.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexcasey.spring_security_template.model.Account;
import com.alexcasey.spring_security_template.request.AuthRequest;
import com.alexcasey.spring_security_template.response.ApiResponse;
import com.alexcasey.spring_security_template.response.JwtResponse;
import com.alexcasey.spring_security_template.security.jwt.JwtUtils;
import com.alexcasey.spring_security_template.security.user.AccountDetails;
import com.alexcasey.spring_security_template.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            AccountDetails userDetails = (AccountDetails) authentication.getPrincipal();
            JwtResponse response = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse("Login successful", response));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRequest request) {
        Account newAccount = accountService.createAccount(request);
        return ResponseEntity.ok(new ApiResponse("Account created", newAccount));
    }

    @GetMapping("/admin-test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> authTest() {
        return ResponseEntity.ok(new ApiResponse("Only an admin can access", null));
    }

    @GetMapping("/user-test")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> loggedInTest() {
        return ResponseEntity.ok(new ApiResponse("Users and admins can access", Map.of("success", true, "message", "You are authenticated")));
    }

    @GetMapping("/hello-guest")
    public ResponseEntity<ApiResponse> guestTest() {
        return ResponseEntity.ok(new ApiResponse("Hello, guest!", null));
    }
}
