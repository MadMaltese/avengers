package com.tableorder.service;

import com.tableorder.dto.request.*;
import com.tableorder.dto.response.OrderResponse;
import com.tableorder.entity.*;
import com.tableorder.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock OrderRepository orderRepository;
    @Mock MenuRepository menuRepository;
    @Mock TableInfoRepository tableInfoRepository;
    @Mock StoreRepository storeRepository;
    @Mock SSEService sseService;
    OrderService orderService;

    Store store; TableInfo table; Menu menu;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, menuRepository, tableInfoRepository, storeRepository, sseService);
        store = new Store(); store.setId(1L);
        table = new TableInfo(); table.setId(1L); table.setTableNo(1); table.setStore(store);
        menu = new Menu(); menu.setId(1L); menu.setName("치킨"); menu.setPrice(18000);
        Category cat = new Category(); cat.setId(1L); cat.setName("메인"); menu.setCategory(cat);
    }

    // TC-BE-014
    @Test void createOrder_firstOrder_startsSession() {
        table.setSessionId(null);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(tableInfoRepository.findById(1L)).thenReturn(Optional.of(table));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(orderRepository.save(any())).thenAnswer(i -> { Order o = i.getArgument(0); o.setId(UUID.randomUUID()); return o; });

        OrderResponse res = orderService.createOrder(1L, 1L, new OrderCreateRequest(List.of(new OrderItemRequest(1L, 2))));
        assertThat(res.id()).isNotNull();
        assertThat(table.getSessionId()).isNotNull().contains("1-");
        assertThat(table.getStatus()).isEqualTo(TableStatus.OCCUPIED);
        assertThat(res.totalAmount()).isEqualTo(36000);
    }

    // TC-BE-015
    @Test void createOrder_existingSession() {
        table.setSessionId("1-20260209120000"); table.setStatus(TableStatus.OCCUPIED);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(tableInfoRepository.findById(1L)).thenReturn(Optional.of(table));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(orderRepository.save(any())).thenAnswer(i -> { Order o = i.getArgument(0); o.setId(UUID.randomUUID()); return o; });

        OrderResponse res = orderService.createOrder(1L, 1L, new OrderCreateRequest(List.of(new OrderItemRequest(1L, 1))));
        assertThat(res.sessionId()).isEqualTo("1-20260209120000");
    }

    // TC-BE-017
    @Test void createOrder_snapshotsMenuNameAndPrice() {
        table.setSessionId("1-20260209120000");
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(tableInfoRepository.findById(1L)).thenReturn(Optional.of(table));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(orderRepository.save(any())).thenAnswer(i -> { Order o = i.getArgument(0); o.setId(UUID.randomUUID()); return o; });

        OrderResponse res = orderService.createOrder(1L, 1L, new OrderCreateRequest(List.of(new OrderItemRequest(1L, 1))));
        assertThat(res.items().get(0).menuName()).isEqualTo("치킨");
        assertThat(res.items().get(0).unitPrice()).isEqualTo(18000);
    }

    // TC-BE-018
    @Test void updateOrderStatus_pendingToPreparing() {
        Order order = createTestOrder(OrderStatus.PENDING);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        OrderResponse res = orderService.updateOrderStatus(order.getId(), OrderStatus.PREPARING);
        assertThat(res.status()).isEqualTo("PREPARING");
    }

    // TC-BE-019
    @Test void updateOrderStatus_preparingToCompleted() {
        Order order = createTestOrder(OrderStatus.PREPARING);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        OrderResponse res = orderService.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);
        assertThat(res.status()).isEqualTo("COMPLETED");
    }

    // TC-BE-020
    @Test void updateOrderStatus_reverseTransitionRejected() {
        Order order = createTestOrder(OrderStatus.COMPLETED);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrderStatus(order.getId(), OrderStatus.PENDING))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("Cannot transition");
    }

    // TC-BE-021
    @Test void deleteOrder_success() {
        Order order = createTestOrder(OrderStatus.PENDING);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.deleteOrder(order.getId());
        verify(orderRepository).delete(order);
    }

    private Order createTestOrder(OrderStatus status) {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStore(store); order.setTable(table);
        order.setSessionId("1-20260209120000");
        order.setStatus(status); order.setTotalAmount(18000);
        order.setItems(new ArrayList<>());
        return order;
    }
}
