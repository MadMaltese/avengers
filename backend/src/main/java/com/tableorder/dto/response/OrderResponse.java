package com.tableorder.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class OrderResponse { private UUID id; private Long tableId; private Integer tableNo; private String sessionId; private String status; private Integer totalAmount; private List<OrderItemResponse> items; private LocalDateTime createdAt; }
