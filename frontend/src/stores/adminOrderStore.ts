import { create } from 'zustand'
import type { OrderResponse, TableResponse } from '../types'
import { orderApi, tableApi } from '../api'

interface AdminOrderState {
  orders: OrderResponse[]
  tables: TableResponse[]
  loading: boolean
  fetchOrders: (storeId: number) => Promise<void>
  fetchTables: (storeId: number) => Promise<void>
  updateOrderInList: (order: OrderResponse) => void
  addOrder: (order: OrderResponse) => void
  removeOrderFromList: (orderId: string) => void
  clearAll: () => void
}

export const useAdminOrderStore = create<AdminOrderState>()((set) => ({
  orders: [], tables: [], loading: false,
  fetchOrders: async (storeId) => {
    set({ loading: true })
    const { data } = await orderApi.getByStore(storeId)
    set({ orders: data, loading: false })
  },
  fetchTables: async (storeId) => {
    const { data } = await tableApi.getAll(storeId)
    set({ tables: data })
  },
  updateOrderInList: (order) => set(state => ({
    orders: state.orders.map(o => o.id === order.id ? order : o)
  })),
  addOrder: (order) => set(state => ({ orders: [order, ...state.orders] })),
  removeOrderFromList: (orderId) => set(state => ({
    orders: state.orders.filter(o => o.id !== orderId)
  })),
  clearAll: () => set({ orders: [], tables: [] }),
}))
