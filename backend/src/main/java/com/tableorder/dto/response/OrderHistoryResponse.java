package com.tableorder.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class OrderHistoryResponse { private UUID id; private String sessionId; private String status; private Integer totalAmount; private List<OrderHistoryItemResponse> items; private LocalDateTime createdAt; private LocalDateTime completedAt; }
