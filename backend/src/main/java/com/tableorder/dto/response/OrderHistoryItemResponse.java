package com.tableorder.dto.response;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class OrderHistoryItemResponse { private String menuName; private Integer quantity; private Integer unitPrice; }
