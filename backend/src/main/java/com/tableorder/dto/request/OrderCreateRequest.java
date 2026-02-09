package com.tableorder.dto.request;
import jakarta.validation.constraints.*;
import java.util.List;
public record OrderCreateRequest(@NotEmpty List<OrderItemRequest> items) {}
