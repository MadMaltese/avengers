package com.tableorder.dto.request;
import jakarta.validation.constraints.*;
public record TableSetupRequest(@NotNull Integer tableNo, @NotBlank String password) {}
