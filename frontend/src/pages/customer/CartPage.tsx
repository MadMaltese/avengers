import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useCartStore } from '../../stores/cartStore'
import { useAuthStore } from '../../stores/authStore'
import { orderApi } from '../../api'
import { ConfirmModal } from '../../components/Common'

export default function CartPage() {
  const { items, updateQuantity, removeItem, clearCart, getTotalPrice } = useCartStore()
  const { storeId, tableId, setSessionId } = useAuthStore()
  const [showConfirm, setShowConfirm] = useState(false)
  const [error, setError] = useState('')
  const [orderSuccess, setOrderSuccess] = useState<string | null>(null)
  const navigate = useNavigate()

  const handleOrder = async () => {
    setShowConfirm(false)
    setError('')
    try {
      const { data } = await orderApi.create(storeId!, tableId!, { items: items.map(i => ({ menuId: i.menuId, quantity: i.quantity })) })
      setSessionId(data.sessionId)
      setOrderSuccess(data.id)
      clearCart()
      setTimeout(() => navigate('/'), 5000)
    } catch { setError('주문 실패. 다시 시도해주세요.') }
  }

  if (orderSuccess) return (
    <div style={{ textAlign: 'center', padding: 40 }}>
      <h2>✅ 주문 완료!</h2>
      <p>주문 번호: {orderSuccess}</p>
      <p>5초 후 메뉴 화면으로 이동합니다...</p>
    </div>
  )

  return (
    <div style={{ padding: 16, maxWidth: 600, margin: '0 auto' }}>
      <h1>장바구니</h1>
      {items.length === 0 ? <p>장바구니가 비어있습니다.</p> : (
        <>
          {items.map(item => (
            <div key={item.menuId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: 12, borderBottom: '1px solid #e2e8f0' }}>
              <div>
                <strong>{item.menuName}</strong>
                <p style={{ margin: 0, color: '#718096' }}>{item.price.toLocaleString()}원</p>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <button onClick={() => updateQuantity(item.menuId, item.quantity - 1)} style={{ minWidth: 44, minHeight: 44, fontSize: 18 }}>-</button>
                <span style={{ minWidth: 24, textAlign: 'center' }}>{item.quantity}</span>
                <button onClick={() => updateQuantity(item.menuId, item.quantity + 1)} style={{ minWidth: 44, minHeight: 44, fontSize: 18 }}>+</button>
                <button onClick={() => removeItem(item.menuId)} style={{ minWidth: 44, minHeight: 44, color: '#e53e3e' }}>✕</button>
              </div>
            </div>
          ))}
          <div style={{ padding: 16, fontWeight: 'bold', fontSize: 18 }}>총 금액: {getTotalPrice().toLocaleString()}원</div>
          {error && <p style={{ color: '#e53e3e' }}>{error}</p>}
          <div style={{ display: 'flex', gap: 8 }}>
            <button onClick={clearCart} style={{ flex: 1, padding: 14, minHeight: 44, border: '1px solid #e2e8f0', background: '#fff', borderRadius: 4 }}>비우기</button>
            <button onClick={() => setShowConfirm(true)} style={{ flex: 2, padding: 14, minHeight: 44, background: '#3182ce', color: '#fff', border: 'none', borderRadius: 4 }}>주문하기</button>
          </div>
        </>
      )}
      {showConfirm && <ConfirmModal message={`총 ${getTotalPrice().toLocaleString()}원을 주문하시겠습니까?`} onConfirm={handleOrder} onCancel={() => setShowConfirm(false)} />}
    </div>
  )
}
