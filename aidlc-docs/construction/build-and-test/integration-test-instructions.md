# Integration Test Instructions

## 목적
Database ↔ Backend ↔ Frontend 간 상호작용을 검증합니다.

## 사전 조건
- PostgreSQL 실행 중, `tableorder` DB 생성됨
- Backend 실행 중 (http://localhost:8080)
- Frontend 실행 중 (http://localhost:5173)

## 테스트 시나리오

### Scenario 1: 테이블 로그인 → 메뉴 조회 → 주문 생성
```bash
# 1. 테이블 로그인
curl -s -X POST http://localhost:8080/api/auth/table-login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE001","tableNo":1,"password":"1234"}'
# 기대: {"token":"...","storeId":1,"tableId":1,"tableNo":1}

# 2. 메뉴 조회 (TOKEN을 위 응답에서 추출)
curl -s http://localhost:8080/api/stores/1/menus \
  -H "Authorization: Bearer {TOKEN}"
# 기대: 메뉴 목록 JSON 배열

# 3. 주문 생성
curl -s -X POST http://localhost:8080/api/stores/1/tables/1/orders \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"items":[{"menuId":1,"quantity":2}]}'
# 기대: 주문 응답 (id, sessionId, status=PENDING, items, totalAmount)
```

### Scenario 2: 관리자 로그인 → 주문 상태 변경 → 테이블 퇴석
```bash
# 1. 관리자 로그인
curl -s -X POST http://localhost:8080/api/auth/admin-login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE001","username":"admin","password":"admin1234"}'
# 기대: {"token":"...","storeId":1}

# 2. 주문 상태 변경 (PENDING → PREPARING)
curl -s -X PATCH http://localhost:8080/api/orders/{ORDER_ID}/status \
  -H "Authorization: Bearer {ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"status":"PREPARING"}'
# 기대: status=PREPARING

# 3. 주문 상태 변경 (PREPARING → COMPLETED)
curl -s -X PATCH http://localhost:8080/api/orders/{ORDER_ID}/status \
  -H "Authorization: Bearer {ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"status":"COMPLETED"}'
# 기대: status=COMPLETED

# 4. 테이블 퇴석
curl -s -X POST http://localhost:8080/api/stores/1/tables/1/complete \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
# 기대: 200 OK, 주문이 order_history로 이동
```

### Scenario 3: 메뉴 CRUD (관리자)
```bash
# 1. 메뉴 추가
curl -s -X POST http://localhost:8080/api/stores/1/menus \
  -H "Authorization: Bearer {ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"name":"새 메뉴","price":12000,"categoryId":1}'
# 기대: 생성된 메뉴 응답

# 2. 메뉴 수정
curl -s -X PUT http://localhost:8080/api/menus/{MENU_ID} \
  -H "Authorization: Bearer {ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"name":"수정된 메뉴","price":13000}'
# 기대: 수정된 메뉴 응답

# 3. 메뉴 삭제
curl -s -X DELETE http://localhost:8080/api/menus/{MENU_ID} \
  -H "Authorization: Bearer {ADMIN_TOKEN}"
# 기대: 200 OK
```

### Scenario 4: SSE 실시간 알림
```bash
# 터미널 1: 관리자 SSE 구독
curl -N http://localhost:8080/api/stores/1/sse/orders?token={ADMIN_TOKEN}

# 터미널 2: 고객 주문 생성
curl -s -X POST http://localhost:8080/api/stores/1/tables/1/orders \
  -H "Authorization: Bearer {TABLE_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"items":[{"menuId":1,"quantity":1}]}'

# 기대: 터미널 1에 새 주문 이벤트 수신
```

## UI 통합 테스트 (수동)
1. http://localhost:5173/customer/setup 접속 → 테이블 로그인
2. 메뉴 페이지에서 메뉴 담기 → 장바구니 → 주문
3. 주문 내역 페이지에서 상태 확인
4. http://localhost:5173/admin/login 접속 → 관리자 로그인
5. 대시보드에서 주문 상태 변경 → 고객 화면 실시간 반영 확인
