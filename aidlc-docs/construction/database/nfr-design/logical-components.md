# Database - 논리적 컴포넌트

## 컴포넌트 구성

```
PostgreSQL 16
  |
  +-- Flyway Migration Engine
  |     +-- V1__create_tables.sql (스키마)
  |     +-- V99__seed_data.sql (시드 데이터)
  |
  +-- HikariCP Connection Pool
  |     +-- max-pool-size: 10
  |     +-- min-idle: 5
  |
  +-- Tables (9개)
        +-- store, admin, table_info
        +-- category, menu
        +-- orders, order_item
        +-- order_history, order_history_item
```
