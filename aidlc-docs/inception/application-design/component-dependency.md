# 테이블오더 서비스 - 컴포넌트 의존성

## Backend 의존성 매트릭스

```
AuthController → AuthService → AdminRepository, TableRepository, JwtUtil
MenuController → MenuService → MenuRepository, CategoryRepository
OrderController → OrderService → OrderRepository, OrderItemRepository, SSEService
TableController → TableService → TableRepository, OrderRepository, OrderHistoryRepository, SSEService
SSEController → SSEService
SecurityConfig → JwtUtil
```

## Backend 의존성 다이어그램

```
+------------------+    +------------------+    +-------------------+
| AuthController   |    | MenuController   |    | OrderController   |
+------------------+    +------------------+    +-------------------+
        |                       |                        |
        v                       v                        v
+------------------+    +------------------+    +-------------------+
| AuthService      |    | MenuService      |    | OrderService      |
+------------------+    +------------------+    +-------------------+
   |          |            |          |            |       |       |
   v          v            v          v            v       v       v
+------+ +-------+  +------+ +------+ +-------+ +-----+ +------+
|Admin | |Table  |  |Menu  | |Categ | |Order  | |Order| |SSE   |
|Repo  | |Repo   |  |Repo  | |Repo  | |Repo   | |Item | |Svc   |
+------+ +-------+  +------+ +------+ +-------+ |Repo | +------+
                                                 +-----+
+------------------+    +-------------------+
| TableController  |    | SSEController     |
+------------------+    +-------------------+
        |                        |
        v                        v
+------------------+    +-------------------+
| TableService     |    | SSEService        |
+------------------+    +-------------------+
   |     |     |     |
   v     v     v     v
+------+ +-------+ +-------+ +------+
|Table | |Order  | |Order  | |SSE   |
|Repo  | |Repo   | |History| |Svc   |
+------+ +-------+ |Repo   | +------+
                    +-------+
```

## Frontend 의존성

```
Pages (Customer)
  MenuPage → useMenuStore, useCartStore
  CartPage → useCartStore
  OrderPage → useOrderStore, useSSE
  SetupPage → useAuthStore

Pages (Admin)
  LoginPage → useAuthStore
  DashboardPage → useAdminOrderStore, useSSE
  MenuManagementPage → useMenuStore

Stores → API Service (apiClient)
API Service → axios + JWT interceptor → Backend REST API
SSE Hook → EventSource → Backend SSE Endpoint
```

## 통신 패턴

| 통신 | 방식 | 설명 |
|------|------|------|
| Frontend → Backend | REST API (HTTP) | 모든 CRUD 요청 |
| Backend → Frontend (실시간) | SSE (Server-Sent Events) | 주문 이벤트 푸시 |
| Frontend 내부 | Zustand Store | 컴포넌트 간 상태 공유 |
| 장바구니 저장 | localStorage | 클라이언트 로컬 persist |

## 데이터 흐름

```
고객 주문 플로우:
  Customer UI → REST POST /api/orders → OrderService
    → OrderRepository (저장)
    → SSEService.broadcastOrderEvent()
      → Admin Dashboard (SSE)
      → Customer Order Page (SSE)

관리자 상태 변경 플로우:
  Admin UI → REST PATCH /api/orders/{id}/status → OrderService
    → OrderRepository (업데이트)
    → SSEService.broadcastOrderEvent()
      → Customer Order Page (SSE)
      → Admin Dashboard (SSE)

이용 완료 플로우:
  Admin UI → REST POST /api/tables/{id}/complete → TableService
    → Order → OrderHistory 이동
    → 테이블 리셋
    → SSEService.broadcastOrderEvent()
```
