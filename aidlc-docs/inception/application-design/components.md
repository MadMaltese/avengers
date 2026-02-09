# 테이블오더 서비스 - 컴포넌트 정의

## Frontend 컴포넌트 (React + TypeScript)

### FE-01: AuthModule
- **책임**: 테이블 태블릿 자동 로그인, 관리자 로그인, JWT 토큰 관리
- **인터페이스**: 로그인 폼, 자동 로그인 처리, 토큰 저장/갱신/만료 처리

### FE-02: MenuModule
- **책임**: 메뉴 목록 조회, 카테고리 탐색, 메뉴 상세 표시
- **인터페이스**: 카테고리 탭, 메뉴 카드 리스트, 메뉴 상세 뷰

### FE-03: CartModule
- **책임**: 장바구니 상태 관리, 수량 조절, 총 금액 계산, 로컬 저장
- **인터페이스**: 장바구니 뷰, 수량 조절 컨트롤, 총 금액 표시

### FE-04: OrderModule
- **책임**: 주문 생성, 주문 확인, 주문 내역 조회, 주문 상태 실시간 수신(SSE)
- **인터페이스**: 주문 확인 화면, 주문 내역 리스트, 상태 배지

### FE-05: AdminDashboardModule
- **책임**: 테이블별 주문 대시보드, 실시간 주문 수신(SSE), 주문 상태 변경
- **인터페이스**: 그리드 레이아웃, 테이블 카드, 주문 상세 모달, 필터

### FE-06: TableManagementModule
- **책임**: 테이블 초기 설정, 주문 삭제, 이용 완료 처리, 과거 내역 조회
- **인터페이스**: 테이블 설정 폼, 확인 팝업, 과거 내역 모달

### FE-07: MenuManagementModule
- **책임**: 메뉴 CRUD, 카테고리 관리, 노출 순서 조정
- **인터페이스**: 메뉴 리스트, 등록/수정 폼, 순서 조정 UI

### FE-08: SSEClient
- **책임**: SSE 연결 관리, 이벤트 수신 및 스토어 업데이트
- **인터페이스**: 연결/해제, 이벤트 리스너 등록

---

## Backend 컴포넌트 (Java + Spring Boot, Layered Architecture)

### BE-01: AuthController / AuthService / AuthRepository
- **책임**: 테이블 인증, 관리자 인증, JWT 발급/검증
- **인터페이스**: POST /api/auth/table-login, POST /api/auth/admin-login

### BE-02: MenuController / MenuService / MenuRepository
- **책임**: 메뉴 CRUD, 카테고리별 조회, 노출 순서 관리
- **인터페이스**: GET/POST/PUT/DELETE /api/menus, GET /api/menus?category=

### BE-03: CartController (없음 - 클라이언트 전용)
- **책임**: 장바구니는 클라이언트 로컬 저장, 서버 컴포넌트 없음

### BE-04: OrderController / OrderService / OrderRepository
- **책임**: 주문 생성, 주문 조회, 주문 상태 변경, 주문 삭제
- **인터페이스**: POST /api/orders, GET /api/orders, PATCH /api/orders/{id}/status, DELETE /api/orders/{id}

### BE-05: TableController / TableService / TableRepository
- **책임**: 테이블 설정, 세션 관리, 이용 완료 처리, 과거 내역 조회
- **인터페이스**: POST /api/tables, POST /api/tables/{id}/complete, GET /api/tables/{id}/history

### BE-06: SSEController / SSEService
- **책임**: SSE 연결 관리, 주문 이벤트 브로드캐스트
- **인터페이스**: GET /api/sse/orders (관리자), GET /api/sse/orders/table/{tableId} (고객)

### BE-07: SecurityConfig
- **책임**: JWT 필터, 인증/인가 설정, CORS 설정, 로그인 시도 제한

---

## 데이터 컴포넌트 (PostgreSQL)

### DB-01: Store 테이블
- **책임**: 매장 정보 저장

### DB-02: Admin 테이블
- **책임**: 관리자 계정 정보 (1매장 = 1관리자)

### DB-03: TableInfo 테이블
- **책임**: 테이블 정보, 세션 상태

### DB-04: Category 테이블
- **책임**: 메뉴 카테고리 정보

### DB-05: Menu 테이블
- **책임**: 메뉴 정보 (카테고리 FK)

### DB-06: Order 테이블
- **책임**: 현재 활성 주문

### DB-07: OrderItem 테이블
- **책임**: 주문 상세 항목 (메뉴별 수량/단가)

### DB-08: OrderHistory 테이블
- **책임**: 이용 완료 처리된 과거 주문 이력
