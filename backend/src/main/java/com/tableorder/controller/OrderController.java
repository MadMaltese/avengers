package com.tableorder.controller;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.OrderResponse;
import com.tableorder.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/stores/{storeId}/tables/{tableId}/orders")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable Long storeId, @PathVariable Long tableId, @Valid @RequestBody OrderCreateRequest req) {
        return ResponseEntity.status(201).body(orderService.createOrder(storeId, tableId, req));
    }

    @GetMapping("/api/stores/{storeId}/tables/{tableId}/orders")
    public List<OrderResponse> getTableOrders(@PathVariable Long storeId, @PathVariable Long tableId, @RequestParam String sessionId) {
        return orderService.getOrdersByTable(storeId, tableId, sessionId);
    }

    @GetMapping("/api/stores/{storeId}/orders")
    public List<OrderResponse> getStoreOrders(@PathVariable Long storeId) {
        return orderService.getOrdersByStore(storeId);
    }

    @PatchMapping("/api/orders/{orderId}/status")
    public OrderResponse updateStatus(@PathVariable UUID orderId, @Valid @RequestBody OrderStatusRequest req) {
        return orderService.updateOrderStatus(orderId, req.status());
    }

    @DeleteMapping("/api/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
