import { describe, it, expect, vi, beforeEach } from 'vitest'
import { renderHook } from '@testing-library/react'
import { useSSE } from '../hooks/useSSE'

// TC-FE-010, TC-FE-011: SSE hook 테스트
const mockClose = vi.fn()
let mockOnMessage: ((e: MessageEvent) => void) | null = null

class MockEventSource {
  constructor(public url: string) {}
  set onmessage(fn: (e: MessageEvent) => void) { mockOnMessage = fn }
  set onerror(_fn: () => void) { /* noop */ }
  close = mockClose
}

vi.stubGlobal('EventSource', MockEventSource)

vi.mock('../stores/authStore', () => ({
  useAuthStore: (selector: (s: { token: string }) => string) => selector({ token: 'test-token' }),
}))

describe('useSSE', () => {
  beforeEach(() => { vi.clearAllMocks(); mockOnMessage = null })

  // TC-FE-010: SSE 연결 및 메시지 수신
  it('connects and receives messages', () => {
    const onMessage = vi.fn()
    renderHook(() => useSSE('/api/sse', onMessage))
    expect(mockOnMessage).not.toBeNull()
    const event = new MessageEvent('message', { data: '{"test":true}' })
    mockOnMessage!(event)
    expect(onMessage).toHaveBeenCalledWith(event)
  })

  // TC-FE-011: 언마운트 시 연결 해제
  it('closes on unmount', () => {
    const { unmount } = renderHook(() => useSSE('/api/sse', vi.fn()))
    unmount()
    expect(mockClose).toHaveBeenCalled()
  })
})
