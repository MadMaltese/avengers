import { create } from 'zustand'
import type { OrderResponse } from '../types'
import { orderApi } from '../api'

interface OrderState {
  orders: OrderResponse[]
  loading: boolean
  fetchOrders: (storeId: number, tableId: number, sessionId: string) => Promise<void>
  updateOrderInList: (order: OrderResponse) => void
  removeOrderFromList: (orderId: string) => void
  clearOrders: () => void
}

export const useOrderStore = create<OrderState>()((set) => ({
  orders: [], loading: false,
  fetchOrders: async (storeId, tableId, sessionId) => {
    set({ loading: true })
    const { data } = await orderApi.getByTable(storeId, tableId, sessionId)
    set({ orders: data, loading: false })
  },
  updateOrderInList: (order) => set(state => ({
    orders: state.orders.map(o => o.id === order.id ? order : o)
  })),
  removeOrderFromList: (orderId) => set(state => ({
    orders: state.orders.filter(o => o.id !== orderId)
  })),
  clearOrders: () => set({ orders: [] }),
}))
