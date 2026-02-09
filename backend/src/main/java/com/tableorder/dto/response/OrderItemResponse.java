package com.tableorder.dto.response;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class OrderItemResponse { private Long id; private String menuName; private Integer quantity; private Integer unitPrice; }
