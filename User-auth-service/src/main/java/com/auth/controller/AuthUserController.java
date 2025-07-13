package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.auth.model.AuthUser;
import com.auth.service.CustomUserDetailsService;


@RestController
@RequestMapping("/auth")
public class AuthUserController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthUser user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        customUserDetailsService.save(user);

        return ResponseEntity.ok("User registered successfully!");

    }

    @GetMapping("/users")
    public AuthUser getUsersDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AuthUser) authentication.getPrincipal();
    }

}
