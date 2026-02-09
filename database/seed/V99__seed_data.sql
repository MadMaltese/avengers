-- ============================================
-- V99__seed_data.sql
-- 테스트용 시드 데이터
-- admin password: 'admin123' -> bcrypt hash
-- table password: '1234' -> bcrypt hash
-- ============================================

-- 매장
INSERT INTO store (code, name) VALUES ('STORE001', '테스트 매장');

-- 관리자 (password: admin123)
INSERT INTO admin (store_id, username, password)
VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- 테이블 5개 (password: 1234)
INSERT INTO table_info (store_id, table_no, password) VALUES
(1, 1, '$2a$10$dXJ3SW6G7P50lGmMQgel6uVktDQd2BZjvCgSc/XyU3IzYBMwqC3Ge'),
(1, 2, '$2a$10$dXJ3SW6G7P50lGmMQgel6uVktDQd2BZjvCgSc/XyU3IzYBMwqC3Ge'),
(1, 3, '$2a$10$dXJ3SW6G7P50lGmMQgel6uVktDQd2BZjvCgSc/XyU3IzYBMwqC3Ge'),
(1, 4, '$2a$10$dXJ3SW6G7P50lGmMQgel6uVktDQd2BZjvCgSc/XyU3IzYBMwqC3Ge'),
(1, 5, '$2a$10$dXJ3SW6G7P50lGmMQgel6uVktDQd2BZjvCgSc/XyU3IzYBMwqC3Ge');

-- 카테고리
INSERT INTO category (store_id, name, sort_order) VALUES
(1, '메인', 1),
(1, '사이드', 2),
(1, '음료', 3);

-- 메뉴 (메인)
INSERT INTO menu (store_id, category_id, name, price, description, sort_order) VALUES
(1, 1, '치킨', 18000, '바삭한 후라이드 치킨', 1),
(1, 1, '피자', 20000, '모짜렐라 치즈 피자', 2),
(1, 1, '파스타', 15000, '크림 파스타', 3);

-- 메뉴 (사이드)
INSERT INTO menu (store_id, category_id, name, price, description, sort_order) VALUES
(1, 2, '감자튀김', 5000, '바삭한 감자튀김', 1),
(1, 2, '샐러드', 7000, '신선한 야채 샐러드', 2),
(1, 2, '치즈스틱', 6000, '모짜렐라 치즈스틱', 3);

-- 메뉴 (음료)
INSERT INTO menu (store_id, category_id, name, price, description, sort_order) VALUES
(1, 3, '콜라', 2000, '코카콜라 500ml', 1),
(1, 3, '사이다', 2000, '칠성사이다 500ml', 2),
(1, 3, '맥주', 5000, '생맥주 500ml', 3);
