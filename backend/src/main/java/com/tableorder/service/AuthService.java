package com.tableorder.service;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.TokenResponse;
import com.tableorder.entity.*;
import com.tableorder.repository.*;
import com.tableorder.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class AuthService {
    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;
    private final TableInfoRepository tableInfoRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse tableLogin(TableLoginRequest req) {
        Store store = storeRepository.findByCode(req.storeCode())
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));
        TableInfo table = tableInfoRepository.findByStoreIdAndTableNo(store.getId(), req.tableNo())
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), table.getPassword()))
            throw new IllegalStateException("Invalid credentials");
        String token = jwtUtil.generateToken(store.getId(), table.getId(), "TABLE", table.getTableNo());
        return new TokenResponse(token, store.getId(), table.getId(), table.getTableNo());
    }

    @Transactional
    public TokenResponse adminLogin(AdminLoginRequest req) {
        Store store = storeRepository.findByCode(req.storeCode())
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));
        Admin admin = adminRepository.findByStoreAndUsername(store, req.username())
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));
        if (admin.getLockedUntil() != null && admin.getLockedUntil().isAfter(LocalDateTime.now()))
            throw new IllegalStateException("Account locked. Try again later.");
        if (!passwordEncoder.matches(req.password(), admin.getPassword())) {
            admin.setFailedAttempts(admin.getFailedAttempts() + 1);
            if (admin.getFailedAttempts() >= 10)
                admin.setLockedUntil(LocalDateTime.now().plusMinutes(30));
            adminRepository.save(admin);
            throw new IllegalStateException("Invalid credentials");
        }
        admin.setFailedAttempts(0);
        admin.setLockedUntil(null);
        adminRepository.save(admin);
        String token = jwtUtil.generateToken(store.getId(), admin.getId(), "ADMIN", null);
        return new TokenResponse(token, store.getId(), null, null);
    }
}
