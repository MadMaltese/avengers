package com.tableorder.controller;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.TokenResponse;
import com.tableorder.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/table-login")
    public ResponseEntity<TokenResponse> tableLogin(@Valid @RequestBody TableLoginRequest req) {
        return ResponseEntity.ok(authService.tableLogin(req));
    }

    @PostMapping("/admin-login")
    public ResponseEntity<TokenResponse> adminLogin(@Valid @RequestBody AdminLoginRequest req) {
        return ResponseEntity.ok(authService.adminLogin(req));
    }
}
