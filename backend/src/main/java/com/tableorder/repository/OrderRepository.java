package com.tableorder.repository;
import com.tableorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByStoreIdAndTableIdAndSessionIdOrderByCreatedAt(Long storeId, Long tableId, String sessionId);
    List<Order> findByStoreIdOrderByCreatedAtDesc(Long storeId);
    List<Order> findByTableIdAndSessionId(Long tableId, String sessionId);
}
