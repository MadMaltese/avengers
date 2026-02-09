package com.tableorder.dto.request;
import jakarta.validation.constraints.*;
public record MenuCreateRequest(@NotBlank String name, @Min(0) @NotNull Integer price, String description, @NotNull Long categoryId) {}
