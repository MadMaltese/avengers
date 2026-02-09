# Test Plan - frontend

## Unit Overview
- **Unit**: frontend
- **Stories**: US-C01~C16, US-A01~A16 (32개)

## Store Layer Tests

### cartStore

#### TC-FE-001: 장바구니에 새 메뉴 추가
- Given: 빈 장바구니
- When: addItem({menuId:1, menuName:"치킨", price:15000})
- Then: items에 {menuId:1, quantity:1} 추가됨
- Story: US-C06
- Status: ⬜ Not Started

#### TC-FE-002: 기존 메뉴 추가 시 수량 증가
- Given: 장바구니에 menuId:1 (quantity:1)
- When: addItem({menuId:1, ...})
- Then: quantity=2
- Story: US-C06
- Status: ⬜ Not Started

#### TC-FE-003: 수량 변경
- Given: 장바구니에 menuId:1 (quantity:2)
- When: updateQuantity(1, 3)
- Then: quantity=3
- Story: US-C07
- Status: ⬜ Not Started

#### TC-FE-004: 수량 0 이하 시 삭제
- Given: 장바구니에 menuId:1 (quantity:1)
- When: updateQuantity(1, 0)
- Then: 항목 삭제됨
- Story: US-C07
- Status: ⬜ Not Started

#### TC-FE-005: 항목 삭제
- Given: 장바구니에 menuId:1
- When: removeItem(1)
- Then: 항목 삭제됨
- Story: US-C08
- Status: ⬜ Not Started

#### TC-FE-006: 총 금액 계산
- Given: 장바구니에 {price:15000, qty:2}, {price:5000, qty:1}
- When: getTotalPrice()
- Then: 35000 반환
- Story: US-C09
- Status: ⬜ Not Started

#### TC-FE-007: 장바구니 비우기
- Given: 장바구니에 항목 3개
- When: clearCart()
- Then: items=[], totalPrice=0
- Story: US-C10
- Status: ⬜ Not Started

### authStore

#### TC-FE-008: 인증 정보 설정
- Given: 빈 authStore
- When: setAuth({token, storeId:1, tableId:1, tableNo:1})
- Then: token, storeId, tableId, role 설정됨
- Story: US-C02
- Status: ⬜ Not Started

#### TC-FE-009: 로그아웃
- Given: 인증된 상태
- When: logout()
- Then: 모든 상태 null로 초기화
- Story: US-A02
- Status: ⬜ Not Started

### useSSE Hook

#### TC-FE-010: SSE 연결 및 이벤트 수신
- Given: SSE URL 제공
- When: useSSE(url) 호출
- Then: EventSource 연결, onmessage 콜백 동작
- Story: US-C16, US-A04
- Status: ⬜ Not Started

#### TC-FE-011: SSE 에러 시 재연결
- Given: SSE 연결 중
- When: 연결 에러 발생
- Then: 3초 후 자동 재연결 시도
- Story: US-C16, US-A04
- Status: ⬜ Not Started

## Component Tests

#### TC-FE-012: ProtectedRoute - 인증된 사용자 접근
- Given: authStore에 유효한 토큰
- When: ProtectedRoute 렌더링
- Then: 자식 컴포넌트 렌더링됨
- Story: US-C02, US-A02
- Status: ⬜ Not Started

#### TC-FE-013: ProtectedRoute - 미인증 사용자 리다이렉트
- Given: authStore 토큰 없음
- When: ProtectedRoute 렌더링
- Then: 로그인 화면으로 리다이렉트
- Story: US-C02, US-A02
- Status: ⬜ Not Started

## Requirements Coverage
| Requirement | Test Cases | Status |
|------------|------------|--------|
| BR-FE01 수량 최소 1 | TC-FE-004 | ⬜ |
| BR-FE02 빈 장바구니 주문 불가 | TC-FE-007 | ⬜ |
| BR-UI05 SSE 재연결 | TC-FE-011 | ⬜ |
| 장바구니 localStorage | TC-FE-001~007 | ⬜ |
| Route Guard | TC-FE-012~013 | ⬜ |
