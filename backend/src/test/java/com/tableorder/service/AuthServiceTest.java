package com.tableorder.service;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.TokenResponse;
import com.tableorder.entity.*;
import com.tableorder.repository.*;
import com.tableorder.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock StoreRepository storeRepository;
    @Mock AdminRepository adminRepository;
    @Mock TableInfoRepository tableInfoRepository;
    @Mock JwtUtil jwtUtil;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    AuthService authService;

    Store store;

    @BeforeEach
    void setUp() {
        authService = new AuthService(storeRepository, adminRepository, tableInfoRepository, jwtUtil, passwordEncoder);
        store = new Store(); store.setId(1L); store.setCode("STORE001");
    }

    // TC-BE-001
    @Test void tableLogin_success() {
        TableInfo table = new TableInfo(); table.setId(1L); table.setTableNo(1);
        table.setPassword(passwordEncoder.encode("1234"));
        when(storeRepository.findByCode("STORE001")).thenReturn(java.util.Optional.of(store));
        when(tableInfoRepository.findByStoreIdAndTableNo(1L, 1)).thenReturn(java.util.Optional.of(table));
        when(jwtUtil.generateToken(1L, 1L, "TABLE", 1)).thenReturn("token");

        TokenResponse res = authService.tableLogin(new TableLoginRequest("STORE001", 1, "1234"));
        assertThat(res.token()).isEqualTo("token");
        assertThat(res.storeId()).isEqualTo(1L);
        assertThat(res.tableId()).isEqualTo(1L);
    }

    // TC-BE-002
    @Test void tableLogin_wrongPassword() {
        TableInfo table = new TableInfo(); table.setPassword(passwordEncoder.encode("1234"));
        when(storeRepository.findByCode("STORE001")).thenReturn(java.util.Optional.of(store));
        when(tableInfoRepository.findByStoreIdAndTableNo(1L, 1)).thenReturn(java.util.Optional.of(table));

        assertThatThrownBy(() -> authService.tableLogin(new TableLoginRequest("STORE001", 1, "wrong")))
                .isInstanceOf(IllegalStateException.class);
    }

    // TC-BE-003
    @Test void adminLogin_success() {
        Admin admin = new Admin(); admin.setId(1L); admin.setStore(store);
        admin.setUsername("admin"); admin.setPassword(passwordEncoder.encode("pass"));
        admin.setFailedAttempts(3);
        when(storeRepository.findByCode("STORE001")).thenReturn(java.util.Optional.of(store));
        when(adminRepository.findByStoreAndUsername(store, "admin")).thenReturn(java.util.Optional.of(admin));
        when(jwtUtil.generateToken(1L, 1L, "ADMIN", null)).thenReturn("token");

        TokenResponse res = authService.adminLogin(new AdminLoginRequest("STORE001", "admin", "pass"));
        assertThat(res.token()).isEqualTo("token");
        assertThat(admin.getFailedAttempts()).isEqualTo(0);
        assertThat(admin.getLockedUntil()).isNull();
    }

    // TC-BE-004
    @Test void adminLogin_failIncrementsCount() {
        Admin admin = new Admin(); admin.setId(1L); admin.setStore(store);
        admin.setPassword(passwordEncoder.encode("pass")); admin.setFailedAttempts(0);
        when(storeRepository.findByCode("STORE001")).thenReturn(java.util.Optional.of(store));
        when(adminRepository.findByStoreAndUsername(store, "admin")).thenReturn(java.util.Optional.of(admin));

        assertThatThrownBy(() -> authService.adminLogin(new AdminLoginRequest("STORE001", "admin", "wrong")))
                .isInstanceOf(IllegalStateException.class);
        assertThat(admin.getFailedAttempts()).isEqualTo(1);
    }

    // TC-BE-005
    @Test void adminLogin_locksAfter10Failures() {
        Admin admin = new Admin(); admin.setId(1L); admin.setStore(store);
        admin.setPassword(passwordEncoder.encode("pass")); admin.setFailedAttempts(9);
        when(storeRepository.findByCode("STORE001")).thenReturn(java.util.Optional.of(store));
        when(adminRepository.findByStoreAndUsername(store, "admin")).thenReturn(java.util.Optional.of(admin));

        assertThatThrownBy(() -> authService.adminLogin(new AdminLoginRequest("STORE001", "admin", "wrong")))
                .isInstanceOf(IllegalStateException.class);
        assertThat(admin.getFailedAttempts()).isEqualTo(10);
        assertThat(admin.getLockedUntil()).isNotNull();
    }

    // TC-BE-006
    @Test void adminLogin_lockedAccount() {
        Admin admin = new Admin(); admin.setId(1L); admin.setStore(store);
        admin.setPassword(passwordEncoder.encode("pass"));
        admin.setLockedUntil(LocalDateTime.now().plusMinutes(30));
        when(storeRepository.findByCode("STORE001")).thenReturn(java.util.Optional.of(store));
        when(adminRepository.findByStoreAndUsername(store, "admin")).thenReturn(java.util.Optional.of(admin));

        assertThatThrownBy(() -> authService.adminLogin(new AdminLoginRequest("STORE001", "admin", "pass")))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("locked");
    }
}
