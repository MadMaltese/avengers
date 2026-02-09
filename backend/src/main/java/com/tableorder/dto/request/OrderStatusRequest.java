package com.tableorder.dto.request;
import com.tableorder.entity.OrderStatus;
import jakarta.validation.constraints.*;
public record OrderStatusRequest(@NotNull OrderStatus status) {}
