# Test Plan - backend

## Unit Overview
- **Unit**: backend
- **Stories**: US-C01~C05, US-C13~C16, US-A01~A16 (28개)

## Business Logic Layer Tests

### AuthService

#### TC-BE-001: 유효한 테이블 로그인
- Given: 매장(STORE001), 테이블(1번, password 설정됨) 존재
- When: tableLogin("STORE001", 1, "correct-password")
- Then: JWT 토큰 반환 (role=TABLE, storeId, tableId, tableNo 포함)
- Story: US-C01, US-C02
- Status: ⬜ Not Started

#### TC-BE-002: 잘못된 테이블 비밀번호
- Given: 매장, 테이블 존재
- When: tableLogin("STORE001", 1, "wrong-password")
- Then: UnauthorizedException 발생
- Story: US-C02
- Status: ⬜ Not Started

#### TC-BE-003: 유효한 관리자 로그인
- Given: 매장(STORE001), 관리자(admin) 존재
- When: adminLogin("STORE001", "admin", "correct-password")
- Then: JWT 토큰 반환 (role=ADMIN), failedAttempts 리셋
- Story: US-A01
- Status: ⬜ Not Started

#### TC-BE-004: 관리자 로그인 실패 카운트
- Given: 매장, 관리자 존재, failedAttempts=0
- When: adminLogin with wrong password
- Then: failedAttempts 증가, UnauthorizedException
- Story: US-A01
- Status: ⬜ Not Started

#### TC-BE-005: 관리자 계정 잠금 (10회 실패)
- Given: 관리자 failedAttempts=9
- When: adminLogin with wrong password
- Then: failedAttempts=10, lockedUntil=now+30분, AccountLockedException
- Story: US-A01
- Status: ⬜ Not Started

#### TC-BE-006: 잠금된 계정 로그인 시도
- Given: 관리자 lockedUntil > now
- When: adminLogin with correct password
- Then: AccountLockedException (올바른 비밀번호여도 거부)
- Story: US-A01
- Status: ⬜ Not Started

### MenuService

#### TC-BE-007: 매장 전체 메뉴 조회
- Given: 매장에 카테고리 3개, 메뉴 9개 존재
- When: getMenusByStore(storeId)
- Then: 9개 메뉴 반환, sortOrder 순 정렬
- Story: US-C03
- Status: ⬜ Not Started

#### TC-BE-008: 카테고리별 메뉴 조회
- Given: 카테고리 "메인"에 메뉴 3개
- When: getMenusByCategory(storeId, categoryId)
- Then: 해당 카테고리 메뉴 3개만 반환
- Story: US-C04
- Status: ⬜ Not Started

#### TC-BE-009: 메뉴 등록 성공
- Given: 유효한 메뉴 데이터 (name, price>=0, categoryId)
- When: createMenu(storeId, request)
- Then: 메뉴 생성, sortOrder 자동 설정
- Story: US-A13
- Status: ⬜ Not Started

#### TC-BE-010: 메뉴 등록 실패 (가격 음수)
- Given: price = -1000
- When: createMenu(storeId, request)
- Then: ValidationException
- Story: US-A13
- Status: ⬜ Not Started

#### TC-BE-011: 메뉴 수정
- Given: 기존 메뉴 존재
- When: updateMenu(menuId, request)
- Then: 메뉴 정보 업데이트됨
- Story: US-A14
- Status: ⬜ Not Started

#### TC-BE-012: 메뉴 삭제
- Given: 기존 메뉴 존재
- When: deleteMenu(menuId)
- Then: 메뉴 삭제됨
- Story: US-A15
- Status: ⬜ Not Started

#### TC-BE-013: 메뉴 순서 변경
- Given: 메뉴 3개 존재
- When: updateMenuOrder(storeId, [{menuId:1, sortOrder:2}, ...])
- Then: sortOrder 업데이트됨
- Story: US-A16
- Status: ⬜ Not Started

### OrderService

#### TC-BE-014: 주문 생성 (첫 주문 - 세션 시작)
- Given: 테이블 session_id=null (빈 테이블)
- When: createOrder(storeId, tableId, {items})
- Then: 주문 생성(UUID), 세션 ID 생성("{tableNo}-{timestamp}"), 테이블 status=OCCUPIED
- Story: US-C13
- Status: ⬜ Not Started

#### TC-BE-015: 주문 생성 (기존 세션)
- Given: 테이블 session_id 존재 (OCCUPIED)
- When: createOrder(storeId, tableId, {items})
- Then: 기존 session_id 사용, 주문 생성
- Story: US-C13
- Status: ⬜ Not Started

#### TC-BE-016: 주문 생성 실패 (빈 항목)
- Given: items = []
- When: createOrder
- Then: ValidationException
- Story: US-C14
- Status: ⬜ Not Started

#### TC-BE-017: 메뉴명/단가 스냅샷
- Given: 메뉴 "치킨" 가격 15000
- When: createOrder with menuId
- Then: OrderItem에 menuName="치킨", unitPrice=15000 저장
- Story: US-C13
- Status: ⬜ Not Started

#### TC-BE-018: 주문 상태 변경 (PENDING→PREPARING)
- Given: 주문 status=PENDING
- When: updateOrderStatus(orderId, PREPARING)
- Then: status=PREPARING
- Story: US-A06
- Status: ⬜ Not Started

#### TC-BE-019: 주문 상태 변경 (PREPARING→COMPLETED)
- Given: 주문 status=PREPARING
- When: updateOrderStatus(orderId, COMPLETED)
- Then: status=COMPLETED
- Story: US-A06
- Status: ⬜ Not Started

#### TC-BE-020: 주문 상태 역방향 전이 거부
- Given: 주문 status=COMPLETED
- When: updateOrderStatus(orderId, PENDING)
- Then: IllegalStateException
- Story: US-A06
- Status: ⬜ Not Started

#### TC-BE-021: 주문 삭제
- Given: 주문 존재
- When: deleteOrder(orderId)
- Then: 주문 + 항목 삭제됨
- Story: US-A09
- Status: ⬜ Not Started

#### TC-BE-022: 테이블 세션 주문 조회
- Given: 테이블에 현재 세션 주문 3개
- When: getOrdersByTable(storeId, tableId, sessionId)
- Then: 해당 세션 주문 3개만 반환
- Story: US-C15
- Status: ⬜ Not Started

### TableService

#### TC-BE-023: 테이블 초기 설정
- Given: 매장 존재
- When: setupTable(storeId, {tableNo:1, password:"1234"})
- Then: 테이블 생성, password bcrypt 해시, status=EMPTY
- Story: US-A08
- Status: ⬜ Not Started

#### TC-BE-024: 이용 완료 (세션 종료)
- Given: 테이블 OCCUPIED, 주문 2개 존재
- When: completeTable(storeId, tableId)
- Then: Order→OrderHistory 이동, 테이블 session_id=null, status=EMPTY
- Story: US-A10
- Status: ⬜ Not Started

#### TC-BE-025: 빈 테이블 이용 완료 시도
- Given: 테이블 session_id=null
- When: completeTable(storeId, tableId)
- Then: IllegalStateException
- Story: US-A10
- Status: ⬜ Not Started

#### TC-BE-026: 과거 주문 내역 조회
- Given: 이용 완료된 주문 이력 존재
- When: getTableHistory(storeId, tableId, null)
- Then: 과거 주문 목록 반환 (시간 역순)
- Story: US-A11
- Status: ⬜ Not Started

#### TC-BE-027: 과거 내역 날짜 필터
- Given: 여러 날짜의 이력 존재
- When: getTableHistory(storeId, tableId, specificDate)
- Then: 해당 날짜 이력만 반환
- Story: US-A11
- Status: ⬜ Not Started

### SSEService

#### TC-BE-028: 관리자 SSE 구독
- Given: 매장 존재
- When: subscribeAdmin(storeId)
- Then: SseEmitter 반환, emitter 목록에 등록
- Story: US-A04
- Status: ⬜ Not Started

#### TC-BE-029: 주문 이벤트 브로드캐스트
- Given: 관리자 SSE 구독 중
- When: broadcastOrderEvent(storeId, NEW_ORDER, data)
- Then: 관리자 emitter에 이벤트 전송됨
- Story: US-A04
- Status: ⬜ Not Started

## Requirements Coverage
| Requirement | Test Cases | Status |
|------------|------------|--------|
| BR-AUTH01 10회 잠금 | TC-BE-005 | ⬜ |
| BR-AUTH02 성공 시 리셋 | TC-BE-003 | ⬜ |
| BR-ORD01 UUID | TC-BE-014 | ⬜ |
| BR-ORD02 순방향 전이 | TC-BE-018~020 | ⬜ |
| BR-ORD04 스냅샷 | TC-BE-017 | ⬜ |
| BR-SES01 세션 ID 형식 | TC-BE-014 | ⬜ |
| BR-SES02 첫 주문 세션 시작 | TC-BE-014 | ⬜ |
| BR-SES03 이용 완료 세션 종료 | TC-BE-024 | ⬜ |
| BR-SES04 이력 이동 | TC-BE-024 | ⬜ |
| BR-MNU01~03 메뉴 검증 | TC-BE-009~010 | ⬜ |
