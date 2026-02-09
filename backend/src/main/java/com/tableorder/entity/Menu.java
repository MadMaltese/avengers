package com.tableorder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private Integer price;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private int sortOrder;
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() { this.createdAt = LocalDateTime.now(); }
}
