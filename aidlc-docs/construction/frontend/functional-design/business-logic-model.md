# Frontend - 비즈니스 로직 모델

## 화면별 상태 흐름

### 고객 플로우
```
[태블릿 열기]
    |
    v
자동 로그인 시도 (localStorage 토큰 확인)
    |-- 성공 → 메뉴 화면 (/)
    |-- 실패 → 초기 설정 화면 (/customer/setup)
    
메뉴 화면 (/)
    |-- 카테고리 탭 선택 → 해당 카테고리 메뉴 표시
    |-- 메뉴 카드 터치 → 메뉴 상세 표시
    |-- 장바구니 추가 → cartStore 업데이트
    |-- 장바구니 아이콘 → 장바구니 화면 (/customer/cart)
    |-- 주문 내역 탭 → 주문 내역 화면 (/customer/orders)

장바구니 화면 (/customer/cart)
    |-- 수량 조절 → cartStore 업데이트 → 총 금액 재계산
    |-- 삭제 → cartStore에서 제거
    |-- 비우기 → cartStore 초기화
    |-- 주문하기 → 주문 확인 모달
        |-- 확정 → POST /api/orders
            |-- 성공 → 주문 번호 표시 → cartStore 비우기 → 메뉴 화면 리다이렉트
            |-- 실패 → 에러 메시지 (장바구니 유지)

주문 내역 화면 (/customer/orders)
    |-- SSE 연결 → 주문 상태 실시간 업데이트
    |-- 현재 세션 주문만 표시
```

### 관리자 플로우
```
[브라우저 열기]
    |
    v
토큰 확인 (localStorage)
    |-- 유효 → 대시보드 (/admin/dashboard)
    |-- 만료/없음 → 로그인 화면 (/admin/login)

로그인 화면 (/admin/login)
    |-- 로그인 → POST /api/auth/admin-login
        |-- 성공 → 토큰 저장 → 대시보드
        |-- 실패 → 에러 메시지
        |-- 잠금 → 잠금 안내 메시지

대시보드 (/admin/dashboard)
    |-- SSE 연결 → 실시간 주문 수신
    |-- 테이블 카드 클릭 → 주문 상세 모달
    |-- 상태 변경 → PATCH /api/orders/{id}/status
    |-- 주문 삭제 → 확인 팝업 → DELETE /api/orders/{id}
    |-- 이용 완료 → 확인 팝업 → POST /api/tables/{id}/complete
    |-- 과거 내역 → 과거 내역 모달
    |-- 필터링 → 테이블별 필터
```

## Zustand Store 상세

### cartStore (localStorage persist)
```
state:
  items: CartItem[]  // {menuId, menuName, price, quantity}
  
actions:
  addItem(menu): 이미 있으면 quantity+1, 없으면 새 항목 추가
  removeItem(menuId): 항목 삭제
  updateQuantity(menuId, qty): qty <= 0이면 삭제, 아니면 수량 변경
  clearCart(): items = []
  
computed:
  totalPrice: items.reduce((sum, item) => sum + item.price * item.quantity, 0)
  totalItems: items.reduce((sum, item) => sum + item.quantity, 0)
```

### authStore (localStorage persist)
```
state:
  token: string | null
  storeId: number | null
  tableId: number | null
  tableNo: number | null
  role: 'TABLE' | 'ADMIN' | null
  
actions:
  setAuth(tokenResponse): 토큰 파싱 후 상태 설정
  logout(): 상태 초기화
  isAuthenticated(): token 존재 및 만료 확인
```
