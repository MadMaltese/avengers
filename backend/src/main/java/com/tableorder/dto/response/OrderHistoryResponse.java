package com.tableorder.dto.response;
import java.time.LocalDateTime;
import java.util.*;
public record OrderHistoryResponse(UUID id, String sessionId, String status, Integer totalAmount, List<OrderHistoryItemResponse> items, LocalDateTime createdAt, LocalDateTime completedAt) {}
