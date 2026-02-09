package com.tableorder.repository;
import com.tableorder.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.*;
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, UUID> {
    List<OrderHistory> findByStoreIdAndTableIdOrderByCompletedAtDesc(Long storeId, Long tableId);
    List<OrderHistory> findByStoreIdAndTableIdAndCompletedAtBetweenOrderByCompletedAtDesc(Long storeId, Long tableId, LocalDateTime start, LocalDateTime end);
}
