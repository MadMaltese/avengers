import { describe, it, expect, beforeEach } from 'vitest'
import { useAuthStore } from '../stores/authStore'

describe('authStore', () => {
  beforeEach(() => useAuthStore.setState({ token: null, storeId: null, tableId: null, tableNo: null, role: null, sessionId: null }))

  // TC-FE-008: 인증 정보 설정
  it('sets auth info', () => {
    useAuthStore.getState().setAuth('token123', 1, 10, 5, 'TABLE')
    const state = useAuthStore.getState()
    expect(state.token).toBe('token123')
    expect(state.storeId).toBe(1)
    expect(state.tableId).toBe(10)
    expect(state.tableNo).toBe(5)
    expect(state.role).toBe('TABLE')
  })

  // TC-FE-009: 로그아웃 시 상태 초기화
  it('clears state on logout', () => {
    useAuthStore.getState().setAuth('token123', 1, 10, 5, 'ADMIN')
    useAuthStore.getState().logout()
    const state = useAuthStore.getState()
    expect(state.token).toBeNull()
    expect(state.storeId).toBeNull()
    expect(state.role).toBeNull()
    expect(state.isAuthenticated()).toBe(false)
  })
})
