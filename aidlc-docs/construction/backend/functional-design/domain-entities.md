# Backend - 도메인 엔티티 (JPA)

## Entity 목록

### Store
```java
@Entity
public class Store {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private String code;
    @Column(nullable = false, length = 100)
    private String name;
    private LocalDateTime createdAt;
}
```

### Admin
```java
@Entity
public class Admin {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @OneToOne @JoinColumn(name = "store_id", unique = true, nullable = false)
    private Store store;
    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = false, length = 255)
    private String password; // bcrypt
    private int failedAttempts;
    private LocalDateTime lockedUntil;
    private LocalDateTime createdAt;
}
```

### TableInfo
```java
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columns = {"store_id", "table_no"}))
public class TableInfo {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @Column(nullable = false)
    private Integer tableNo;
    @Column(nullable = false, length = 255)
    private String password; // bcrypt
    @Column(length = 50)
    private String sessionId; // null = EMPTY
    @Enumerated(STRING) @Column(nullable = false)
    private TableStatus status; // EMPTY, OCCUPIED
    private LocalDateTime createdAt;
}
```

### Category
```java
@Entity
public class Category {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @Column(nullable = false, length = 50)
    private String name;
    private int sortOrder;
}
```

### Menu
```java
@Entity
public class Menu {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private Integer price; // >= 0
    private String description;
    private int sortOrder;
    private LocalDateTime createdAt;
}
```

### Order
```java
@Entity @Table(name = "orders")
public class Order {
    @Id
    private UUID id; // gen_random_uuid()
    @ManyToOne @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne @JoinColumn(name = "table_id", nullable = false)
    private TableInfo table;
    @Column(nullable = false, length = 50)
    private String sessionId;
    @Enumerated(STRING) @Column(nullable = false)
    private OrderStatus status; // PENDING, PREPARING, COMPLETED
    @Column(nullable = false)
    private Integer totalAmount;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> items;
}
```

### OrderItem
```java
@Entity
public class OrderItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @Column(nullable = false, length = 100)
    private String menuName; // 스냅샷
    @Column(nullable = false)
    private Integer quantity; // > 0
    @Column(nullable = false)
    private Integer unitPrice; // 스냅샷, >= 0
}
```

### OrderHistory
```java
@Entity
public class OrderHistory {
    @Id
    private UUID id; // 원본 Order ID
    private Long storeId;
    private Long tableId;
    @Column(nullable = false, length = 50)
    private String sessionId;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Integer totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    @OneToMany(mappedBy = "history", cascade = ALL, orphanRemoval = true)
    private List<OrderHistoryItem> items;
}
```

### OrderHistoryItem
```java
@Entity
public class OrderHistoryItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "history_id", nullable = false)
    private OrderHistory history;
    @Column(nullable = false, length = 100)
    private String menuName;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer unitPrice;
}
```

## Enum 정의

### OrderStatus
```java
public enum OrderStatus {
    PENDING, PREPARING, COMPLETED;

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING -> next == PREPARING;
            case PREPARING -> next == COMPLETED;
            case COMPLETED -> false;
        };
    }
}
```

### TableStatus
```java
public enum TableStatus {
    EMPTY, OCCUPIED
}
```
