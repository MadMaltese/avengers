import { create } from 'zustand'
import type { MenuResponse, CategoryResponse } from '../types'
import { menuApi } from '../api'

interface MenuState {
  menus: MenuResponse[]
  categories: CategoryResponse[]
  loading: boolean
  fetchMenus: (storeId: number, categoryId?: number) => Promise<void>
  fetchCategories: (storeId: number) => Promise<void>
}

export const useMenuStore = create<MenuState>()((set) => ({
  menus: [], categories: [], loading: false,
  fetchMenus: async (storeId, categoryId) => {
    set({ loading: true })
    const { data } = await menuApi.getMenus(storeId, categoryId)
    set({ menus: data, loading: false })
  },
  fetchCategories: async (storeId) => {
    const { data } = await menuApi.getCategories(storeId)
    set({ categories: data })
  },
}))
