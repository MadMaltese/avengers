# 테이블오더 서비스 - 컴포넌트 메서드 시그니처

> 상세 비즈니스 로직은 Functional Design 단계에서 정의

---

## Backend 메서드 시그니처

### AuthService
| 메서드 | 입력 | 출력 | 목적 |
|--------|------|------|------|
| `tableLogin(storeCode, tableNo, password)` | TableLoginRequest | TokenResponse | 테이블 태블릿 인증 |
| `adminLogin(storeCode, username, password)` | AdminLoginRequest | TokenResponse | 관리자 인증 |
| `validateToken(token)` | String | AuthInfo | JWT 토큰 검증 |

### MenuService
| 메서드 | 입력 | 출력 | 목적 |
|--------|------|------|------|
| `getMenusByStore(storeId)` | Long | List\<MenuResponse\> | 매장 전체 메뉴 조회 |
| `getMenusByCategory(storeId, categoryId)` | Long, Long | List\<MenuResponse\> | 카테고리별 메뉴 조회 |
| `createMenu(storeId, request)` | Long, MenuCreateRequest | MenuResponse | 메뉴 등록 |
| `updateMenu(menuId, request)` | Long, MenuUpdateRequest | MenuResponse | 메뉴 수정 |
| `deleteMenu(menuId)` | Long | void | 메뉴 삭제 |
| `updateMenuOrder(storeId, request)` | Long, MenuOrderRequest | void | 노출 순서 변경 |
| `getCategories(storeId)` | Long | List\<CategoryResponse\> | 카테고리 목록 조회 |

### OrderService
| 메서드 | 입력 | 출력 | 목적 |
|--------|------|------|------|
| `createOrder(storeId, tableId, request)` | Long, Long, OrderCreateRequest | OrderResponse | 주문 생성 |
| `getOrdersByTable(storeId, tableId, sessionId)` | Long, Long, String | List\<OrderResponse\> | 테이블 세션 주문 조회 |
| `getOrdersByStore(storeId)` | Long | List\<OrderResponse\> | 매장 전체 주문 조회 |
| `updateOrderStatus(orderId, status)` | Long, OrderStatus | OrderResponse | 주문 상태 변경 |
| `deleteOrder(orderId)` | Long | void | 주문 삭제 |

### TableService
| 메서드 | 입력 | 출력 | 목적 |
|--------|------|------|------|
| `setupTable(storeId, request)` | Long, TableSetupRequest | TableResponse | 테이블 초기 설정 |
| `completeTable(storeId, tableId)` | Long, Long | void | 이용 완료 (세션 종료) |
| `getTableHistory(storeId, tableId, dateFilter)` | Long, Long, LocalDate | List\<OrderHistoryResponse\> | 과거 내역 조회 |
| `getTablesByStore(storeId)` | Long | List\<TableResponse\> | 매장 테이블 목록 |

### SSEService
| 메서드 | 입력 | 출력 | 목적 |
|--------|------|------|------|
| `subscribeAdmin(storeId)` | Long | SseEmitter | 관리자 SSE 구독 |
| `subscribeTable(storeId, tableId)` | Long, Long | SseEmitter | 고객 테이블 SSE 구독 |
| `broadcastOrderEvent(storeId, event)` | Long, OrderEvent | void | 주문 이벤트 브로드캐스트 |

---

## Frontend 주요 함수/Hook

### Auth
| 함수 | 목적 |
|------|------|
| `useAuth()` | 인증 상태 관리 (Zustand store) |
| `tableLogin(storeCode, tableNo, password)` | 테이블 로그인 API 호출 |
| `adminLogin(storeCode, username, password)` | 관리자 로그인 API 호출 |

### Menu
| 함수 | 목적 |
|------|------|
| `useMenuStore()` | 메뉴 상태 관리 (Zustand store) |
| `fetchMenus(storeId)` | 메뉴 목록 API 호출 |
| `fetchCategories(storeId)` | 카테고리 목록 API 호출 |

### Cart
| 함수 | 목적 |
|------|------|
| `useCartStore()` | 장바구니 상태 관리 (Zustand store + localStorage persist) |
| `addItem(menu)` | 장바구니 추가 |
| `removeItem(menuId)` | 장바구니 삭제 |
| `updateQuantity(menuId, quantity)` | 수량 변경 |
| `clearCart()` | 장바구니 비우기 |
| `getTotalPrice()` | 총 금액 계산 |

### Order
| 함수 | 목적 |
|------|------|
| `useOrderStore()` | 주문 상태 관리 (Zustand store) |
| `createOrder(items)` | 주문 생성 API 호출 |
| `fetchOrders()` | 주문 내역 API 호출 |

### SSE
| 함수 | 목적 |
|------|------|
| `useSSE(url)` | SSE 연결 관리 Hook |
