import apiClient from './apiClient'
import type { TokenResponse, TableLoginRequest, AdminLoginRequest, MenuResponse, CategoryResponse, OrderResponse, OrderCreateRequest, MenuCreateRequest, MenuUpdateRequest, TableResponse, OrderHistoryResponse, OrderStatus } from '../types'

export const authApi = {
  tableLogin: (data: TableLoginRequest) => apiClient.post<TokenResponse>('/auth/table-login', data),
  adminLogin: (data: AdminLoginRequest) => apiClient.post<TokenResponse>('/auth/admin-login', data),
}

export const menuApi = {
  getMenus: (storeId: number, category?: number) => apiClient.get<MenuResponse[]>(`/stores/${storeId}/menus`, { params: category ? { category } : {} }),
  getCategories: (storeId: number) => apiClient.get<CategoryResponse[]>(`/stores/${storeId}/categories`),
  create: (storeId: number, data: MenuCreateRequest) => apiClient.post<MenuResponse>(`/stores/${storeId}/menus`, data),
  update: (menuId: number, data: MenuUpdateRequest) => apiClient.put<MenuResponse>(`/menus/${menuId}`, data),
  remove: (menuId: number) => apiClient.delete(`/menus/${menuId}`),
  updateOrder: (storeId: number, items: { menuId: number; sortOrder: number }[]) => apiClient.put(`/stores/${storeId}/menus/order`, { items }),
}

export const orderApi = {
  create: (storeId: number, tableId: number, data: OrderCreateRequest) => apiClient.post<OrderResponse>(`/stores/${storeId}/tables/${tableId}/orders`, data),
  getByTable: (storeId: number, tableId: number, sessionId: string) => apiClient.get<OrderResponse[]>(`/stores/${storeId}/tables/${tableId}/orders`, { params: { sessionId } }),
  getByStore: (storeId: number) => apiClient.get<OrderResponse[]>(`/stores/${storeId}/orders`),
  updateStatus: (orderId: string, status: OrderStatus) => apiClient.patch<OrderResponse>(`/orders/${orderId}/status`, { status }),
  remove: (orderId: string) => apiClient.delete(`/orders/${orderId}`),
}

export const tableApi = {
  setup: (storeId: number, data: { tableNo: number; password: string }) => apiClient.post<TableResponse>(`/stores/${storeId}/tables`, data),
  getAll: (storeId: number) => apiClient.get<TableResponse[]>(`/stores/${storeId}/tables`),
  complete: (storeId: number, tableId: number) => apiClient.post(`/stores/${storeId}/tables/${tableId}/complete`),
  getHistory: (storeId: number, tableId: number, date?: string) => apiClient.get<OrderHistoryResponse[]>(`/stores/${storeId}/tables/${tableId}/history`, { params: date ? { date } : {} }),
}
