-- ============================================
-- Schema Validation Tests
-- Run after V1__create_tables.sql
-- Each query should return expected results
-- ============================================

-- TC-DB-001: store 테이블 존재 확인
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'store' ORDER BY ordinal_position;

-- TC-DB-002: admin FK 및 UNIQUE 확인
SELECT constraint_name, constraint_type
FROM information_schema.table_constraints
WHERE table_name = 'admin' AND constraint_type IN ('FOREIGN KEY', 'UNIQUE');

-- TC-DB-003: table_info UNIQUE(store_id, table_no) 확인
SELECT constraint_name, constraint_type
FROM information_schema.table_constraints
WHERE table_name = 'table_info' AND constraint_type = 'UNIQUE';

-- TC-DB-004: menu price CHECK 제약조건 테스트
-- 이 INSERT는 실패해야 함 (price < 0)
-- INSERT INTO menu (store_id, category_id, name, price) VALUES (1, 1, 'test', -1);

-- TC-DB-005: order_item quantity CHECK 제약조건 테스트
-- 이 INSERT는 실패해야 함 (quantity <= 0)
-- INSERT INTO order_item (order_id, menu_id, menu_name, quantity, unit_price) VALUES ('00000000-0000-0000-0000-000000000001', 1, 'test', 0, 1000);

-- TC-DB-006: orders CASCADE 삭제 확인
SELECT constraint_name, delete_rule
FROM information_schema.referential_constraints
WHERE constraint_name LIKE '%order_item%order%';

-- TC-DB-007: category RESTRICT 삭제 확인
SELECT constraint_name, delete_rule
FROM information_schema.referential_constraints
WHERE constraint_name LIKE '%menu%category%';

-- TC-DB-008: 시드 데이터 검증 (V99 실행 후)
-- SELECT COUNT(*) FROM store;          -- 1
-- SELECT COUNT(*) FROM admin;          -- 1
-- SELECT COUNT(*) FROM table_info;     -- 5
-- SELECT COUNT(*) FROM category;       -- 3
-- SELECT COUNT(*) FROM menu;           -- 9
