import { BrowserRouter, Routes, Route, Link, useLocation } from 'react-router-dom'
import { useAuthStore } from './stores/authStore'
import { ProtectedRoute } from './components/ProtectedRoute'
import SetupPage from './pages/customer/SetupPage'
import MenuPage from './pages/customer/MenuPage'
import CartPage from './pages/customer/CartPage'
import OrderPage from './pages/customer/OrderPage'
import LoginPage from './pages/admin/LoginPage'
import DashboardPage from './pages/admin/DashboardPage'
import MenuManagementPage from './pages/admin/MenuManagementPage'

function Nav() {
  const { role, logout } = useAuthStore()
  const location = useLocation()
  const isAdmin = location.pathname.startsWith('/admin')

  if (!role) return null

  if (isAdmin && role === 'ADMIN') return (
    <nav style={{ display: 'flex', gap: 16, padding: '12px 16px', background: '#2d3748', color: '#fff', alignItems: 'center' }}>
      <Link to="/admin/dashboard" style={{ color: '#fff', textDecoration: 'none' }}>대시보드</Link>
      <Link to="/admin/menus" style={{ color: '#fff', textDecoration: 'none' }}>메뉴 관리</Link>
      <button onClick={logout} style={{ marginLeft: 'auto', background: 'none', border: '1px solid #fff', color: '#fff', padding: '4px 12px', borderRadius: 4, cursor: 'pointer' }}>로그아웃</button>
    </nav>
  )

  if (role === 'TABLE') return (
    <nav style={{ display: 'flex', gap: 16, padding: '12px 16px', background: '#3182ce', color: '#fff', alignItems: 'center' }}>
      <Link to="/" style={{ color: '#fff', textDecoration: 'none' }}>메뉴</Link>
      <Link to="/customer/cart" style={{ color: '#fff', textDecoration: 'none' }}>장바구니</Link>
      <Link to="/customer/orders" style={{ color: '#fff', textDecoration: 'none' }}>주문내역</Link>
    </nav>
  )

  return null
}

export default function App() {
  return (
    <BrowserRouter>
      <Nav />
      <Routes>
        <Route path="/customer/setup" element={<SetupPage />} />
        <Route path="/" element={<ProtectedRoute role="TABLE"><MenuPage /></ProtectedRoute>} />
        <Route path="/customer/cart" element={<ProtectedRoute role="TABLE"><CartPage /></ProtectedRoute>} />
        <Route path="/customer/orders" element={<ProtectedRoute role="TABLE"><OrderPage /></ProtectedRoute>} />
        <Route path="/admin/login" element={<LoginPage />} />
        <Route path="/admin/dashboard" element={<ProtectedRoute role="ADMIN"><DashboardPage /></ProtectedRoute>} />
        <Route path="/admin/menus" element={<ProtectedRoute role="ADMIN"><MenuManagementPage /></ProtectedRoute>} />
      </Routes>
    </BrowserRouter>
  )
}
