# Backend - 비즈니스 규칙

## 인증 규칙

| 규칙 ID | 규칙 | 설명 |
|---------|------|------|
| BR-AUTH01 | 관리자 로그인 실패 10회 시 30분 잠금 | failed_attempts >= 10 → locked_until = now() + 30분 |
| BR-AUTH02 | 로그인 성공 시 실패 횟수 초기화 | failed_attempts = 0, locked_until = null |
| BR-AUTH03 | JWT 만료 시간 16시간 | exp = now() + 16h |
| BR-AUTH04 | 비밀번호 bcrypt 해싱 | 저장 시 BCryptPasswordEncoder 사용 |

## 주문 규칙

| 규칙 ID | 규칙 | 설명 |
|---------|------|------|
| BR-ORD01 | 주문 ID는 UUID | gen_random_uuid() |
| BR-ORD02 | 상태 전이: 순방향만 | PENDING→PREPARING→COMPLETED만 허용 |
| BR-ORD03 | 주문 항목 최소 1개 | items 비어있으면 400 |
| BR-ORD04 | 메뉴명/단가 스냅샷 | 주문 시점의 메뉴명, 가격을 OrderItem에 복사 |
| BR-ORD05 | 총 금액 자동 계산 | SUM(quantity * unit_price) |

## 세션 규칙

| 규칙 ID | 규칙 | 설명 |
|---------|------|------|
| BR-SES01 | 세션 ID 형식 | "{tableNo}-{yyyyMMddHHmmss}" |
| BR-SES02 | 첫 주문 시 세션 자동 시작 | session_id가 null이면 새 세션 생성 |
| BR-SES03 | 이용 완료 시 세션 종료 | session_id = null, status = EMPTY |
| BR-SES04 | 이용 완료 시 주문 이력 이동 | Order → OrderHistory, OrderItem → OrderHistoryItem |
| BR-SES05 | 자동 만료 없음 | 관리자 수동 종료만 |

## 메뉴 규칙

| 규칙 ID | 규칙 | 설명 |
|---------|------|------|
| BR-MNU01 | 메뉴명 필수 | NOT BLANK |
| BR-MNU02 | 가격 >= 0 | 음수 불가 |
| BR-MNU03 | 카테고리 존재 확인 | 유효한 category_id 필수 |
| BR-MNU04 | 메뉴가 있는 카테고리 삭제 불가 | RESTRICT |

## API 검증 규칙

| 엔드포인트 | 검증 |
|-----------|------|
| POST /api/auth/table-login | storeCode, tableNo, password 필수 |
| POST /api/auth/admin-login | storeCode, username, password 필수 |
| POST /api/orders | items 비어있지 않음, 각 item: menuId, quantity > 0 |
| POST /api/menus | name 필수, price >= 0, categoryId 유효 |
| PATCH /api/orders/{id}/status | status IN (PREPARING, COMPLETED), 순방향 전이만 |
