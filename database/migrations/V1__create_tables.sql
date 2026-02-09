-- ============================================
-- V1__create_tables.sql
-- 테이블오더 서비스 스키마
-- ============================================

-- store
CREATE TABLE store (
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(50) NOT NULL UNIQUE,
    name        VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- admin (1 store = 1 admin)
CREATE TABLE admin (
    id              BIGSERIAL PRIMARY KEY,
    store_id        BIGINT NOT NULL UNIQUE REFERENCES store(id) ON DELETE CASCADE,
    username        VARCHAR(50) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    failed_attempts INT NOT NULL DEFAULT 0,
    locked_until    TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- table_info
CREATE TABLE table_info (
    id          BIGSERIAL PRIMARY KEY,
    store_id    BIGINT NOT NULL REFERENCES store(id) ON DELETE CASCADE,
    table_no    INT NOT NULL,
    password    VARCHAR(255) NOT NULL,
    session_id  VARCHAR(50),
    status      VARCHAR(20) NOT NULL DEFAULT 'EMPTY' CHECK (status IN ('EMPTY', 'OCCUPIED')),
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (store_id, table_no)
);

-- category
CREATE TABLE category (
    id          BIGSERIAL PRIMARY KEY,
    store_id    BIGINT NOT NULL REFERENCES store(id) ON DELETE CASCADE,
    name        VARCHAR(50) NOT NULL,
    sort_order  INT NOT NULL DEFAULT 0
);

-- menu
CREATE TABLE menu (
    id          BIGSERIAL PRIMARY KEY,
    store_id    BIGINT NOT NULL REFERENCES store(id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES category(id) ON DELETE RESTRICT,
    name        VARCHAR(100) NOT NULL,
    price       INT NOT NULL CHECK (price >= 0),
    description TEXT,
    sort_order  INT NOT NULL DEFAULT 0,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- orders
CREATE TABLE orders (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id     BIGINT NOT NULL REFERENCES store(id),
    table_id     BIGINT NOT NULL REFERENCES table_info(id),
    session_id   VARCHAR(50) NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PREPARING', 'COMPLETED')),
    total_amount INT NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

-- order_item
CREATE TABLE order_item (
    id         BIGSERIAL PRIMARY KEY,
    order_id   UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_id    BIGINT NOT NULL REFERENCES menu(id),
    menu_name  VARCHAR(100) NOT NULL,
    quantity   INT NOT NULL CHECK (quantity > 0),
    unit_price INT NOT NULL CHECK (unit_price >= 0)
);

-- order_history
CREATE TABLE order_history (
    id           UUID PRIMARY KEY,
    store_id     BIGINT NOT NULL,
    table_id     BIGINT NOT NULL,
    session_id   VARCHAR(50) NOT NULL,
    status       VARCHAR(20) NOT NULL,
    total_amount INT NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    completed_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- order_history_item
CREATE TABLE order_history_item (
    id         BIGSERIAL PRIMARY KEY,
    history_id UUID NOT NULL REFERENCES order_history(id) ON DELETE CASCADE,
    menu_name  VARCHAR(100) NOT NULL,
    quantity   INT NOT NULL,
    unit_price INT NOT NULL
);

-- Indexes
CREATE INDEX idx_orders_store_session ON orders(store_id, session_id);
CREATE INDEX idx_orders_store_table ON orders(store_id, table_id);
CREATE INDEX idx_order_history_store_table ON order_history(store_id, table_id);
CREATE INDEX idx_order_history_completed ON order_history(completed_at);
CREATE INDEX idx_menu_store_category ON menu(store_id, category_id);
CREATE INDEX idx_menu_sort ON menu(store_id, category_id, sort_order);
