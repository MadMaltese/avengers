package com.tableorder.dto.request;
import jakarta.validation.constraints.*;
public record OrderItemRequest(@NotNull Long menuId, @Min(1) @NotNull Integer quantity) {}
