package com.tableorder.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderHistory {
    @Id @Column(columnDefinition = "VARCHAR(36)")
    @org.hibernate.annotations.Type(type = "uuid-char")
    private UUID id;
    @Column(nullable = false)
    private Long storeId;
    @Column(nullable = false)
    private Long tableId;
    @Column(nullable = false, length = 50)
    private String sessionId;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Integer totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderHistoryItem> items = new ArrayList<>();
}
