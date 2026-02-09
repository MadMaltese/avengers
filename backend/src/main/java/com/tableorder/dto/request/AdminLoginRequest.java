package com.tableorder.dto.request;
import jakarta.validation.constraints.*;
public record AdminLoginRequest(@NotBlank String storeCode, @NotBlank String username, @NotBlank String password) {}
