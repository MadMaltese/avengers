# Frontend - 도메인 엔티티 (TypeScript 타입)

## API Response 타입

```typescript
interface TokenResponse {
  token: string;
  storeId: number;
  tableId?: number;
  tableNo?: number;
}

interface CategoryResponse {
  id: number;
  name: string;
  sortOrder: number;
}

interface MenuResponse {
  id: number;
  categoryId: number;
  name: string;
  price: number;
  description: string | null;
  sortOrder: number;
}

interface OrderResponse {
  id: string; // UUID
  tableId: number;
  tableNo: number;
  sessionId: string;
  status: OrderStatus;
  totalAmount: number;
  items: OrderItemResponse[];
  createdAt: string; // ISO 8601
}

interface OrderItemResponse {
  id: number;
  menuName: string;
  quantity: number;
  unitPrice: number;
}

interface TableResponse {
  id: number;
  tableNo: number;
  sessionId: string | null;
  status: TableStatus;
}

interface OrderHistoryResponse {
  id: string;
  sessionId: string;
  status: string;
  totalAmount: number;
  items: OrderHistoryItemResponse[];
  createdAt: string;
  completedAt: string;
}

interface OrderHistoryItemResponse {
  menuName: string;
  quantity: number;
  unitPrice: number;
}
```

## API Request 타입

```typescript
interface TableLoginRequest {
  storeCode: string;
  tableNo: number;
  password: string;
}

interface AdminLoginRequest {
  storeCode: string;
  username: string;
  password: string;
}

interface OrderCreateRequest {
  items: OrderItemRequest[];
}

interface OrderItemRequest {
  menuId: number;
  quantity: number;
}

interface MenuCreateRequest {
  name: string;
  price: number;
  description?: string;
  categoryId: number;
}

interface MenuUpdateRequest {
  name?: string;
  price?: number;
  description?: string;
  categoryId?: number;
}

interface MenuOrderUpdateRequest {
  items: { menuId: number; sortOrder: number }[];
}
```

## 클라이언트 전용 타입

```typescript
interface CartItem {
  menuId: number;
  menuName: string;
  price: number;
  quantity: number;
}

type OrderStatus = 'PENDING' | 'PREPARING' | 'COMPLETED';
type TableStatus = 'EMPTY' | 'OCCUPIED';

interface SSEOrderEvent {
  type: 'NEW_ORDER' | 'STATUS_CHANGED' | 'ORDER_DELETED' | 'TABLE_COMPLETED';
  data: OrderResponse | { orderId: string; tableId: number };
}
```
