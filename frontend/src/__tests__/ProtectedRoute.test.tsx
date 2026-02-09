import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { ProtectedRoute } from '../components/ProtectedRoute'
import { useAuthStore } from '../stores/authStore'

describe('ProtectedRoute', () => {
  // TC-FE-012: 인증된 사용자 접근 허용
  it('renders children when authenticated with correct role', () => {
    useAuthStore.setState({ token: 'test', role: 'TABLE', storeId: 1, tableId: 1, tableNo: 1, sessionId: null })
    render(
      <MemoryRouter>
        <ProtectedRoute role="TABLE"><div>Protected Content</div></ProtectedRoute>
      </MemoryRouter>
    )
    expect(screen.getByText('Protected Content')).toBeInTheDocument()
  })

  // TC-FE-013: 미인증 사용자 리다이렉트
  it('redirects when not authenticated', () => {
    useAuthStore.setState({ token: null, role: null, storeId: null, tableId: null, tableNo: null, sessionId: null })
    render(
      <MemoryRouter initialEntries={['/admin/dashboard']}>
        <ProtectedRoute role="ADMIN"><div>Admin Content</div></ProtectedRoute>
      </MemoryRouter>
    )
    expect(screen.queryByText('Admin Content')).not.toBeInTheDocument()
  })
})
