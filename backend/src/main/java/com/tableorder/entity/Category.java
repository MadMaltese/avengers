package com.tableorder.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false)
    private int sortOrder;
}
