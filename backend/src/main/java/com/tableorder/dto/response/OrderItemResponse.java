package com.tableorder.dto.response;
public record OrderItemResponse(Long id, String menuName, Integer quantity, Integer unitPrice) {}
