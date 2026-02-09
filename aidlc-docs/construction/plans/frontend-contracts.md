# Contract/Interface Definition - frontend

## Unit Context
- **Stories**: US-C01~C16, US-A01~A16 (32개 전체)
- **Dependencies**: backend (REST API, SSE)

## Store Layer (Zustand)

### authStore (persist: localStorage)
- `setAuth(tokenResponse) -> void`: 토큰 파싱 후 상태 설정
- `logout() -> void`: 상태 초기화
- `isAuthenticated() -> boolean`: 토큰 유효성 확인

### cartStore (persist: localStorage)
- `addItem(menu) -> void`: 장바구니 추가 (기존이면 수량+1)
- `removeItem(menuId) -> void`: 항목 삭제
- `updateQuantity(menuId, qty) -> void`: 수량 변경 (0이하 시 삭제)
- `clearCart() -> void`: 전체 비우기
- `getTotalPrice() -> number`: 총 금액 계산
- `getTotalItems() -> number`: 총 수량 계산

### menuStore
- `fetchMenus(storeId) -> void`: 메뉴 목록 API 호출
- `fetchCategories(storeId) -> void`: 카테고리 목록 API 호출

### orderStore
- `createOrder(items) -> OrderResponse`: 주문 생성 API 호출
- `fetchOrders() -> void`: 주문 내역 API 호출

## Hook Layer

### useSSE(url)
- EventSource 연결 관리
- onmessage → 콜백 호출
- onerror → 3초 후 자동 재연결
- cleanup → 연결 해제

## API Layer (axios)

### apiClient
- baseURL 설정
- Request interceptor: Bearer 토큰 자동 첨부
- Response interceptor: 401 → logout + 리다이렉트

## Page Layer
- Customer: MenuPage, CartPage, OrderPage, SetupPage
- Admin: LoginPage, DashboardPage, MenuManagementPage

## Component Layer
- ProtectedRoute (역할 기반 라우트 가드)
- ConfirmModal, LoadingSpinner, ErrorMessage
