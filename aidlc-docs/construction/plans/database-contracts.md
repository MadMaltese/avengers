# Contract/Interface Definition - database

## Unit Context
- **Stories**: US-C01, US-C03, US-C13, US-A01, US-A08, US-A10
- **Dependencies**: 없음 (독립 유닛)
- **Database Entities**: store, admin, table_info, category, menu, orders, order_item, order_history, order_history_item

## Database Migration Layer

### V1__create_tables.sql
- store 테이블 생성 (id, code, name, created_at)
- admin 테이블 생성 (id, store_id FK, username, password, failed_attempts, locked_until, created_at)
- table_info 테이블 생성 (id, store_id FK, table_no, password, session_id, status, created_at)
- category 테이블 생성 (id, store_id FK, name, sort_order)
- menu 테이블 생성 (id, store_id FK, category_id FK, name, price, description, sort_order, created_at)
- orders 테이블 생성 (id UUID, store_id FK, table_id FK, session_id, status, total_amount, created_at)
- order_item 테이블 생성 (id, order_id FK, menu_id FK, menu_name, quantity, unit_price)
- order_history 테이블 생성 (id UUID, store_id, table_id, session_id, status, total_amount, created_at, completed_at)
- order_history_item 테이블 생성 (id, history_id FK, menu_name, quantity, unit_price)
- 인덱스 생성 (6개)
- CHECK 제약조건, UNIQUE 제약조건

### V99__seed_data.sql
- 테스트 매장 1개 (code: 'STORE001')
- 관리자 1명 (username: 'admin', password: bcrypt hash)
- 테이블 5개 (1~5번)
- 카테고리 3개 (메인, 사이드, 음료)
- 메뉴 9개 (카테고리별 3개)
