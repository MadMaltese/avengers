package com.tableorder.service;

import com.tableorder.dto.request.TableSetupRequest;
import com.tableorder.dto.response.*;
import com.tableorder.entity.*;
import com.tableorder.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service @RequiredArgsConstructor
public class TableService {
    private final TableInfoRepository tableInfoRepository;
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryItemRepository orderHistoryItemRepository;
    private final StoreRepository storeRepository;
    private final SSEService sseService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TableResponse setupTable(Long storeId, TableSetupRequest req) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found"));
        TableInfo table = new TableInfo();
        table.setStore(store);
        table.setTableNo(req.tableNo());
        table.setPassword(passwordEncoder.encode(req.password()));
        table.setStatus(TableStatus.EMPTY);
        TableInfo saved = tableInfoRepository.save(table);
        return toResponse(saved);
    }

    @Transactional
    public void completeTable(Long storeId, Long tableId) {
        TableInfo table = tableInfoRepository.findById(tableId).orElseThrow(() -> new EntityNotFoundException("Table not found"));
        if (table.getSessionId() == null) throw new IllegalStateException("Table is already empty");

        List<Order> orders = orderRepository.findByTableIdAndSessionId(tableId, table.getSessionId());
        for (Order order : orders) {
            OrderHistory history = new OrderHistory();
            history.setId(order.getId());
            history.setStoreId(storeId);
            history.setTableId(tableId);
            history.setSessionId(order.getSessionId());
            history.setStatus(order.getStatus().name());
            history.setTotalAmount(order.getTotalAmount());
            history.setCreatedAt(order.getCreatedAt());
            history.setCompletedAt(LocalDateTime.now());
            for (OrderItem item : order.getItems()) {
                OrderHistoryItem hi = new OrderHistoryItem();
                hi.setHistory(history);
                hi.setMenuName(item.getMenuName());
                hi.setQuantity(item.getQuantity());
                hi.setUnitPrice(item.getUnitPrice());
                history.getItems().add(hi);
            }
            orderHistoryRepository.save(history);
        }
        orderRepository.deleteAll(orders);

        table.setSessionId(null);
        table.setStatus(TableStatus.EMPTY);
        tableInfoRepository.save(table);
        sseService.broadcastOrderEvent(storeId, "TABLE_COMPLETED", Map.of("tableId", tableId));
    }

    public List<OrderHistoryResponse> getTableHistory(Long storeId, Long tableId, LocalDate dateFilter) {
        List<OrderHistory> histories;
        if (dateFilter != null) {
            LocalDateTime start = dateFilter.atStartOfDay();
            LocalDateTime end = dateFilter.plusDays(1).atStartOfDay();
            histories = orderHistoryRepository.findByStoreIdAndTableIdAndCompletedAtBetweenOrderByCompletedAtDesc(storeId, tableId, start, end);
        } else {
            histories = orderHistoryRepository.findByStoreIdAndTableIdOrderByCompletedAtDesc(storeId, tableId);
        }
        return histories.stream().map(this::toHistoryResponse).toList();
    }

    public List<TableResponse> getTablesByStore(Long storeId) {
        return tableInfoRepository.findByStoreId(storeId).stream().map(this::toResponse).toList();
    }

    private TableResponse toResponse(TableInfo t) {
        return new TableResponse(t.getId(), t.getTableNo(), t.getSessionId(), t.getStatus().name());
    }

    private OrderHistoryResponse toHistoryResponse(OrderHistory h) {
        var items = h.getItems().stream().map(i -> new OrderHistoryItemResponse(i.getMenuName(), i.getQuantity(), i.getUnitPrice())).toList();
        return new OrderHistoryResponse(h.getId(), h.getSessionId(), h.getStatus(), h.getTotalAmount(), items, h.getCreatedAt(), h.getCompletedAt());
    }
}
