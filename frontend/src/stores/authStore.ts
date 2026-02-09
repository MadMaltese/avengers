import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface AuthState {
  token: string | null
  storeId: number | null
  tableId: number | null
  tableNo: number | null
  role: 'TABLE' | 'ADMIN' | null
  sessionId: string | null
  setAuth: (token: string, storeId: number, tableId?: number, tableNo?: number, role?: 'TABLE' | 'ADMIN') => void
  setSessionId: (sessionId: string) => void
  logout: () => void
  isAuthenticated: () => boolean
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      token: null, storeId: null, tableId: null, tableNo: null, role: null, sessionId: null,
      setAuth: (token, storeId, tableId, tableNo, role) => set({ token, storeId, tableId: tableId ?? null, tableNo: tableNo ?? null, role: role ?? null }),
      setSessionId: (sessionId) => set({ sessionId }),
      logout: () => set({ token: null, storeId: null, tableId: null, tableNo: null, role: null, sessionId: null }),
      isAuthenticated: () => !!get().token,
    }),
    { name: 'auth-storage' }
  )
)
