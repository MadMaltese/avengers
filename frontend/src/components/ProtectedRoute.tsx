import { Navigate } from 'react-router-dom'
import { useAuthStore } from '../stores/authStore'

export function ProtectedRoute({ children, role }: { children: React.ReactNode; role: 'TABLE' | 'ADMIN' }) {
  const { token, role: userRole } = useAuthStore()
  if (!token) return <Navigate to={role === 'ADMIN' ? '/admin/login' : '/customer/setup'} />
  if (userRole !== role) return <Navigate to="/" />
  return <>{children}</>
}
