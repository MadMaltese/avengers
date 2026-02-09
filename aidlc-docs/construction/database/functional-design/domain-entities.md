# Database - 도메인 엔티티

## ERD (Entity Relationship Diagram)

```
+----------+       +----------+       +-----------+
|  store   |1----1 |  admin   |       | category  |
+----------+       +----------+       +-----------+
| id (PK)  |       | id (PK)  |       | id (PK)   |
| code     |       | store_id |       | store_id  |
| name     |       | username |       | name      |
+----------+       | password |       | sort_order|
     |              +----------+       +-----------+
     |1                                     |1
     |                                      |
     |N                                     |N
+-----------+                          +----------+
| table_info|                          |   menu   |
+-----------+                          +----------+
| id (PK)   |                         | id (PK)  |
| store_id  |                         | store_id |
| table_no  |                         | categ_id |
| password  |                         | name     |
| session_id|                         | price    |
| status    |                         | desc     |
+-----------+                         | sort_order|
     |1                                +----------+
     |
     |N
+-----------+       +-------------+
|   order   |1----N | order_item  |
+-----------+       +-------------+
| id (PK)   |       | id (PK)     |
| store_id  |       | order_id    |
| table_id  |       | menu_id     |
| session_id|       | menu_name   |
| order_no  |       | quantity    |
| status    |       | unit_price  |
| total_amt |       +-------------+
| created_at|
+-----------+
     |
     | (이용 완료 시 이동)
     v
+---------------+    +--------------------+
| order_history |1-N | order_history_item |
+---------------+    +--------------------+
| id (PK)       |    | id (PK)            |
| store_id      |    | history_id         |
| table_id      |    | menu_name          |
| session_id    |    | quantity            |
| order_no      |    | unit_price          |
| status        |    +--------------------+
| total_amt     |
| created_at    |
| completed_at  |
+---------------+
```

## 테이블 상세 정의

### store
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGSERIAL | PK | 매장 ID |
| code | VARCHAR(50) | UNIQUE, NOT NULL | 매장 식별 코드 |
| name | VARCHAR(100) | NOT NULL | 매장명 |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

### admin
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGSERIAL | PK | 관리자 ID |
| store_id | BIGINT | FK(store.id), UNIQUE, NOT NULL | 매장 (1:1) |
| username | VARCHAR(50) | NOT NULL | 사용자명 |
| password | VARCHAR(255) | NOT NULL | bcrypt 해시 비밀번호 |
| failed_attempts | INT | DEFAULT 0 | 로그인 실패 횟수 |
| locked_until | TIMESTAMP | NULLABLE | 잠금 해제 시각 |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

### table_info
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGSERIAL | PK | 테이블 ID |
| store_id | BIGINT | FK(store.id), NOT NULL | 매장 |
| table_no | INT | NOT NULL | 테이블 번호 |
| password | VARCHAR(255) | NOT NULL | bcrypt 해시 비밀번호 |
| session_id | VARCHAR(50) | NULLABLE | 현재 세션 ID (null=빈 테이블) |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'EMPTY' | EMPTY/OCCUPIED |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| | | UNIQUE(store_id, table_no) | 매장 내 테이블 번호 유니크 |

### category
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGSERIAL | PK | 카테고리 ID |
| store_id | BIGINT | FK(store.id), NOT NULL | 매장 |
| name | VARCHAR(50) | NOT NULL | 카테고리명 |
| sort_order | INT | NOT NULL, DEFAULT 0 | 노출 순서 |

### menu
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGSERIAL | PK | 메뉴 ID |
| store_id | BIGINT | FK(store.id), NOT NULL | 매장 |
| category_id | BIGINT | FK(category.id), NOT NULL | 카테고리 |
| name | VARCHAR(100) | NOT NULL | 메뉴명 |
| price | INT | NOT NULL, CHECK(price >= 0) | 가격 |
| description | TEXT | NULLABLE | 설명 |
| sort_order | INT | NOT NULL, DEFAULT 0 | 노출 순서 |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

### orders
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK, DEFAULT gen_random_uuid() | 주문 ID (UUID) |
| store_id | BIGINT | FK(store.id), NOT NULL | 매장 |
| table_id | BIGINT | FK(table_info.id), NOT NULL | 테이블 |
| session_id | VARCHAR(50) | NOT NULL | 테이블 세션 ID |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING' | PENDING/PREPARING/COMPLETED |
| total_amount | INT | NOT NULL | 총 금액 |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | 주문 시각 |

### order_item
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGSERIAL | PK | 항목 ID |
| order_id | UUID | FK(orders.id), NOT NULL | 주문 |
| menu_id | BIGINT | FK(menu.id), NOT NULL | 메뉴 |
| menu_name | VARCHAR(100) | NOT NULL | 주문 시점 메뉴명 (스냅샷) |
| quantity | INT | NOT NULL, CHECK(quantity > 0) | 수량 |
| unit_price | INT | NOT NULL, CHECK(unit_price >= 0) | 주문 시점 단가 (스냅샷) |

### order_history
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 원본 주문 ID |
| store_id | BIGINT | NOT NULL | 매장 |
| table_id | BIGINT | NOT NULL | 테이블 |
| session_id | VARCHAR(50) | NOT NULL | 세션 ID |
| status | VARCHAR(20) | NOT NULL | 최종 상태 |
| total_amount | INT | NOT NULL | 총 금액 |
| created_at | TIMESTAMP | NOT NULL | 원본 주문 시각 |
| completed_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | 이용 완료 시각 |

### order_history_item
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGSERIAL | PK | 항목 ID |
| history_id | UUID | FK(order_history.id), NOT NULL | 이력 주문 |
| menu_name | VARCHAR(100) | NOT NULL | 메뉴명 |
| quantity | INT | NOT NULL | 수량 |
| unit_price | INT | NOT NULL | 단가 |
