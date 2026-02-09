package com.tableorder.dto.request;
import jakarta.validation.constraints.*;
public record TableLoginRequest(@NotBlank String storeCode, @NotNull Integer tableNo, @NotBlank String password) {}
