package com.tableorder.dto.response;
import java.time.LocalDateTime;
import java.util.*;
public record OrderResponse(UUID id, Long tableId, Integer tableNo, String sessionId, String status, Integer totalAmount, List<OrderItemResponse> items, LocalDateTime createdAt) {}
