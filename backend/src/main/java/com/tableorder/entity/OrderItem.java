package com.tableorder.entity;

import javax.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @Column(nullable = false, length = 100)
    private String menuName;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer unitPrice;
}
