export interface TokenResponse { token: string; storeId: number; tableId?: number; tableNo?: number }
export interface CategoryResponse { id: number; name: string; sortOrder: number }
export interface MenuResponse { id: number; categoryId: number; categoryName: string; name: string; price: number; description: string | null; sortOrder: number }
export interface OrderResponse { id: string; tableId: number; tableNo: number; sessionId: string; status: OrderStatus; totalAmount: number; items: OrderItemResponse[]; createdAt: string }
export interface OrderItemResponse { id: number; menuName: string; quantity: number; unitPrice: number }
export interface TableResponse { id: number; tableNo: number; sessionId: string | null; status: string }
export interface OrderHistoryResponse { id: string; sessionId: string; status: string; totalAmount: number; items: OrderHistoryItemResponse[]; createdAt: string; completedAt: string }
export interface OrderHistoryItemResponse { menuName: string; quantity: number; unitPrice: number }
export interface ErrorResponse { error: string; message: string }

export type OrderStatus = 'PENDING' | 'PREPARING' | 'COMPLETED'

export interface CartItem { menuId: number; menuName: string; price: number; quantity: number }

export interface SSEOrderEvent { type: string; data: unknown }

export interface TableLoginRequest { storeCode: string; tableNo: number; password: string }
export interface AdminLoginRequest { storeCode: string; username: string; password: string }
export interface OrderCreateRequest { items: { menuId: number; quantity: number }[] }
export interface MenuCreateRequest { name: string; price: number; description?: string; categoryId: number }
export interface MenuUpdateRequest { name?: string; price?: number; description?: string; categoryId?: number }
