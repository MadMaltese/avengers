package com.tableorder.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "orders") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Order {
    @Id
    private UUID id;
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne @JoinColumn(name = "table_id", nullable = false)
    private TableInfo table;
    @Column(nullable = false, length = 50)
    private String sessionId;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    @Column(nullable = false)
    private Integer totalAmount;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }
}
