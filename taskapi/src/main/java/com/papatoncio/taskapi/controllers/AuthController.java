package com.papatoncio.taskapi.controllers;

import com.papatoncio.taskapi.dto.auth.AuthRequest;
import com.papatoncio.taskapi.dto.common.ApiResponse;
import com.papatoncio.taskapi.dto.user.UserRequest;
import com.papatoncio.taskapi.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody UserRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody AuthRequest req) {
        return authService.login(req);
    }
}
