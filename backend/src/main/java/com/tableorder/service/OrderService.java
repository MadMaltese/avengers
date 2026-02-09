package com.tableorder.service;

import com.tableorder.dto.request.OrderCreateRequest;
import com.tableorder.dto.response.*;
import com.tableorder.entity.*;
import com.tableorder.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service @RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final TableInfoRepository tableInfoRepository;
    private final StoreRepository storeRepository;
    private final SSEService sseService;

    @Transactional
    public OrderResponse createOrder(Long storeId, Long tableId, OrderCreateRequest req) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found"));
        TableInfo table = tableInfoRepository.findById(tableId).orElseThrow(() -> new EntityNotFoundException("Table not found"));

        if (table.getSessionId() == null) {
            String sessionId = table.getTableNo() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            table.setSessionId(sessionId);
            table.setStatus(TableStatus.OCCUPIED);
            tableInfoRepository.save(table);
        }

        Order order = new Order();
        order.setStore(store);
        order.setTable(table);
        order.setSessionId(table.getSessionId());
        order.setStatus(OrderStatus.PENDING);

        int total = 0;
        for (var itemReq : req.items()) {
            Menu menu = menuRepository.findById(itemReq.menuId()).orElseThrow(() -> new EntityNotFoundException("Menu not found: " + itemReq.menuId()));
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenu(menu);
            item.setMenuName(menu.getName());
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(menu.getPrice());
            order.getItems().add(item);
            total += itemReq.quantity() * menu.getPrice();
        }
        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        OrderResponse response = toResponse(saved);
        sseService.broadcastOrderEvent(storeId, "NEW_ORDER", response);
        sseService.broadcastToTable(storeId, tableId, "NEW_ORDER", response);
        return response;
    }

    public List<OrderResponse> getOrdersByTable(Long storeId, Long tableId, String sessionId) {
        return orderRepository.findByStoreIdAndTableIdAndSessionIdOrderByCreatedAt(storeId, tableId, sessionId).stream().map(this::toResponse).toList();
    }

    public List<OrderResponse> getOrdersByStore(Long storeId) {
        return orderRepository.findByStoreIdOrderByCreatedAtDesc(storeId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!order.getStatus().canTransitionTo(newStatus))
            throw new IllegalStateException("Cannot transition from " + order.getStatus() + " to " + newStatus);
        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);
        OrderResponse response = toResponse(saved);
        sseService.broadcastOrderEvent(saved.getStore().getId(), "STATUS_CHANGED", response);
        sseService.broadcastToTable(saved.getStore().getId(), saved.getTable().getId(), "STATUS_CHANGED", response);
        return response;
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        Long storeId = order.getStore().getId();
        Long tableId = order.getTable().getId();
        orderRepository.delete(order);
        sseService.broadcastOrderEvent(storeId, "ORDER_DELETED", Map.of("orderId", orderId, "tableId", tableId));
    }

    private OrderResponse toResponse(Order o) {
        var items = o.getItems().stream().map(i -> new OrderItemResponse(i.getId(), i.getMenuName(), i.getQuantity(), i.getUnitPrice())).toList();
        return new OrderResponse(o.getId(), o.getTable().getId(), o.getTable().getTableNo(), o.getSessionId(), o.getStatus().name(), o.getTotalAmount(), items, o.getCreatedAt());
    }
}
