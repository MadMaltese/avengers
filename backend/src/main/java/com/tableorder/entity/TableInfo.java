package com.tableorder.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "table_no"}))
public class TableInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @Column(nullable = false)
    private Integer tableNo;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(length = 50)
    private String sessionId;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private TableStatus status = TableStatus.EMPTY;
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() { this.createdAt = LocalDateTime.now(); }
}
