# Test Plan - database

## Unit Overview
- **Unit**: database
- **Stories**: US-C01, US-C03, US-C13, US-A01, US-A08, US-A10

## Schema Validation Tests

### TC-DB-001: store 테이블 생성
- Given: 빈 데이터베이스
- When: V1 마이그레이션 실행
- Then: store 테이블이 id(PK), code(UNIQUE), name, created_at 컬럼으로 생성됨
- Story: US-A01
- Status: ⬜ Not Started

### TC-DB-002: admin 테이블 생성 및 FK
- Given: store 테이블 존재
- When: admin 레코드 삽입
- Then: store_id FK 제약조건 동작, UNIQUE(store_id) 제약조건 동작
- Story: US-A01
- Status: ⬜ Not Started

### TC-DB-003: table_info UNIQUE 제약조건
- Given: store, table_info 테이블 존재
- When: 동일 (store_id, table_no) 삽입 시도
- Then: UNIQUE 제약조건 위반 에러
- Story: US-C01, US-A08
- Status: ⬜ Not Started

### TC-DB-004: menu price CHECK 제약조건
- Given: menu 테이블 존재
- When: price < 0 삽입 시도
- Then: CHECK 제약조건 위반 에러
- Story: US-C03
- Status: ⬜ Not Started

### TC-DB-005: order_item quantity CHECK 제약조건
- Given: order_item 테이블 존재
- When: quantity <= 0 삽입 시도
- Then: CHECK 제약조건 위반 에러
- Story: US-C13
- Status: ⬜ Not Started

### TC-DB-006: orders CASCADE 삭제
- Given: orders + order_item 레코드 존재
- When: orders 레코드 삭제
- Then: 관련 order_item도 CASCADE 삭제됨
- Story: US-A10
- Status: ⬜ Not Started

### TC-DB-007: category RESTRICT 삭제
- Given: category + menu 레코드 존재
- When: category 삭제 시도
- Then: RESTRICT로 삭제 거부됨
- Story: US-C03
- Status: ⬜ Not Started

### TC-DB-008: 시드 데이터 검증
- Given: V1 + V99 마이그레이션 실행
- When: 데이터 조회
- Then: 매장 1개, 관리자 1명, 테이블 5개, 카테고리 3개, 메뉴 9개 존재
- Story: 전체
- Status: ⬜ Not Started

## Requirements Coverage
| Requirement | Test Cases | Status |
|------------|------------|--------|
| BR-DB01 store.code UNIQUE | TC-DB-001 | ⬜ |
| BR-DB02 admin.store_id UNIQUE | TC-DB-002 | ⬜ |
| BR-DB03 (store_id, table_no) UNIQUE | TC-DB-003 | ⬜ |
| BR-DB04 menu.price >= 0 | TC-DB-004 | ⬜ |
| BR-DB05 order_item.quantity > 0 | TC-DB-005 | ⬜ |
| BR-DB06 CASCADE 삭제 | TC-DB-006 | ⬜ |
| BR-DB07 RESTRICT 삭제 | TC-DB-007 | ⬜ |
