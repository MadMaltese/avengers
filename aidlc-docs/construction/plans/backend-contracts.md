# Contract/Interface Definition - backend

## Unit Context
- **Stories**: US-C01~C05, US-C13~C16, US-A01~A16 (28개)
- **Dependencies**: database (스키마)
- **Database Entities**: 전체 9개 테이블

## Business Logic Layer

### AuthService
- `tableLogin(TableLoginRequest) -> TokenResponse`: 테이블 태블릿 인증
  - Args: storeCode, tableNo, password
  - Returns: JWT 토큰 + storeId + tableId + tableNo
  - Raises: UnauthorizedException (잘못된 인증 정보)

- `adminLogin(AdminLoginRequest) -> TokenResponse`: 관리자 인증
  - Args: storeCode, username, password
  - Returns: JWT 토큰 + storeId
  - Raises: UnauthorizedException, AccountLockedException (10회 실패 시)

### MenuService
- `getMenusByStore(Long storeId) -> List<MenuResponse>`: 매장 전체 메뉴 조회
- `getMenusByCategory(Long storeId, Long categoryId) -> List<MenuResponse>`: 카테고리별 조회
- `getCategories(Long storeId) -> List<CategoryResponse>`: 카테고리 목록
- `createMenu(Long storeId, MenuCreateRequest) -> MenuResponse`: 메뉴 등록
  - Raises: ValidationException (필수 필드 누락, 가격 음수)
- `updateMenu(Long menuId, MenuUpdateRequest) -> MenuResponse`: 메뉴 수정
  - Raises: EntityNotFoundException
- `deleteMenu(Long menuId) -> void`: 메뉴 삭제
  - Raises: EntityNotFoundException
- `updateMenuOrder(Long storeId, MenuOrderRequest) -> void`: 노출 순서 변경

### OrderService
- `createOrder(Long storeId, Long tableId, OrderCreateRequest) -> OrderResponse`: 주문 생성
  - 세션 자동 시작 (첫 주문 시), SSE 이벤트 발행
  - Raises: ValidationException (빈 항목, 유효하지 않은 menuId)
- `getOrdersByTable(Long storeId, Long tableId, String sessionId) -> List<OrderResponse>`: 테이블 세션 주문 조회
- `getOrdersByStore(Long storeId) -> List<OrderResponse>`: 매장 전체 주문 조회
- `updateOrderStatus(UUID orderId, OrderStatus newStatus) -> OrderResponse`: 상태 변경
  - Raises: IllegalStateException (역방향 전이), EntityNotFoundException
- `deleteOrder(UUID orderId) -> void`: 주문 삭제
  - Raises: EntityNotFoundException

### TableService
- `setupTable(Long storeId, TableSetupRequest) -> TableResponse`: 테이블 초기 설정
- `completeTable(Long storeId, Long tableId) -> void`: 이용 완료 (세션 종료)
  - Order→OrderHistory 이동, 테이블 리셋
  - Raises: IllegalStateException (빈 테이블), EntityNotFoundException
- `getTableHistory(Long storeId, Long tableId, LocalDate dateFilter) -> List<OrderHistoryResponse>`: 과거 내역
- `getTablesByStore(Long storeId) -> List<TableResponse>`: 매장 테이블 목록

### SSEService
- `subscribeAdmin(Long storeId) -> SseEmitter`: 관리자 SSE 구독
- `subscribeTable(Long storeId, Long tableId) -> SseEmitter`: 고객 SSE 구독
- `broadcastOrderEvent(Long storeId, OrderEvent) -> void`: 이벤트 브로드캐스트

## API Layer

### AuthController
- `POST /api/auth/table-login`: 테이블 로그인
- `POST /api/auth/admin-login`: 관리자 로그인

### MenuController
- `GET /api/stores/{storeId}/menus`: 메뉴 목록 (category 파라미터 선택)
- `GET /api/stores/{storeId}/categories`: 카테고리 목록
- `POST /api/stores/{storeId}/menus`: 메뉴 등록 (ADMIN)
- `PUT /api/menus/{menuId}`: 메뉴 수정 (ADMIN)
- `DELETE /api/menus/{menuId}`: 메뉴 삭제 (ADMIN)
- `PUT /api/stores/{storeId}/menus/order`: 순서 변경 (ADMIN)

### OrderController
- `POST /api/stores/{storeId}/tables/{tableId}/orders`: 주문 생성 (TABLE)
- `GET /api/stores/{storeId}/tables/{tableId}/orders?sessionId=`: 테이블 주문 조회 (TABLE)
- `GET /api/stores/{storeId}/orders`: 매장 전체 주문 (ADMIN)
- `PATCH /api/orders/{orderId}/status`: 상태 변경 (ADMIN)
- `DELETE /api/orders/{orderId}`: 주문 삭제 (ADMIN)

### TableController
- `POST /api/stores/{storeId}/tables`: 테이블 설정 (ADMIN)
- `GET /api/stores/{storeId}/tables`: 테이블 목록 (ADMIN)
- `POST /api/stores/{storeId}/tables/{tableId}/complete`: 이용 완료 (ADMIN)
- `GET /api/stores/{storeId}/tables/{tableId}/history?date=`: 과거 내역 (ADMIN)

### SSEController
- `GET /api/stores/{storeId}/sse/orders`: 관리자 SSE (ADMIN)
- `GET /api/stores/{storeId}/tables/{tableId}/sse/orders`: 고객 SSE (TABLE)

## Repository Layer (Spring Data JPA)
- StoreRepository: `findByCode(String code) -> Optional<Store>`
- AdminRepository: `findByStoreAndUsername(Store, String) -> Optional<Admin>`
- TableInfoRepository: `findByStoreIdAndTableNo(Long, Integer) -> Optional<TableInfo>`
- CategoryRepository: `findByStoreIdOrderBySortOrder(Long) -> List<Category>`
- MenuRepository: `findByStoreIdOrderByCategorySortOrderAscSortOrderAsc(Long) -> List<Menu>`
- OrderRepository: `findByStoreIdAndTableIdAndSessionId(Long, Long, String) -> List<Order>`
- OrderHistoryRepository: `findByStoreIdAndTableIdOrderByCompletedAtDesc(Long, Long) -> List<OrderHistory>`
