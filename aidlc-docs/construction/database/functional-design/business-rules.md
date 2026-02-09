# Database - 비즈니스 규칙

## 제약조건

| 규칙 | 테이블 | 설명 |
|------|--------|------|
| BR-DB01 | store | code는 UNIQUE |
| BR-DB02 | admin | store_id는 UNIQUE (1매장 = 1관리자) |
| BR-DB03 | table_info | (store_id, table_no) UNIQUE |
| BR-DB04 | menu | price >= 0 |
| BR-DB05 | order_item | quantity > 0, unit_price >= 0 |
| BR-DB06 | orders | status IN ('PENDING', 'PREPARING', 'COMPLETED') |
| BR-DB07 | table_info | status IN ('EMPTY', 'OCCUPIED') |

## 인덱스

| 인덱스 | 테이블 | 컬럼 | 목적 |
|--------|--------|------|------|
| idx_orders_store_session | orders | store_id, session_id | 세션별 주문 조회 |
| idx_orders_store_table | orders | store_id, table_id | 테이블별 주문 조회 |
| idx_order_history_store_table | order_history | store_id, table_id | 과거 내역 조회 |
| idx_order_history_completed | order_history | completed_at | 날짜 필터링 |
| idx_menu_store_category | menu | store_id, category_id | 카테고리별 메뉴 조회 |
| idx_menu_sort | menu | store_id, category_id, sort_order | 정렬된 메뉴 조회 |

## 캐스케이드 규칙

| FK | ON DELETE | 설명 |
|----|-----------|------|
| admin.store_id → store.id | CASCADE | 매장 삭제 시 관리자도 삭제 |
| table_info.store_id → store.id | CASCADE | 매장 삭제 시 테이블도 삭제 |
| menu.category_id → category.id | RESTRICT | 메뉴가 있는 카테고리 삭제 방지 |
| order_item.order_id → orders.id | CASCADE | 주문 삭제 시 항목도 삭제 |
| order_history_item.history_id → order_history.id | CASCADE | 이력 삭제 시 항목도 삭제 |
