import { useEffect, useCallback } from 'react'
import { useAuthStore } from '../../stores/authStore'
import { useOrderStore } from '../../stores/orderStore'
import { useSSE } from '../../hooks/useSSE'
import { LoadingSpinner } from '../../components/Common'
import type { OrderResponse } from '../../types'

const STATUS_LABEL: Record<string, string> = { PENDING: '대기중', PREPARING: '준비중', COMPLETED: '완료' }
const STATUS_COLOR: Record<string, string> = { PENDING: '#ed8936', PREPARING: '#3182ce', COMPLETED: '#38a169' }

export default function OrderPage() {
  const { storeId, tableId, sessionId } = useAuthStore()
  const { orders, loading, fetchOrders, updateOrderInList } = useOrderStore()

  useEffect(() => {
    if (storeId && tableId && sessionId) fetchOrders(storeId, tableId, sessionId)
  }, [storeId, tableId, sessionId, fetchOrders])

  const handleSSE = useCallback((event: MessageEvent) => {
    try {
      const order: OrderResponse = JSON.parse(event.data)
      updateOrderInList(order)
    } catch { /* ignore parse errors */ }
  }, [updateOrderInList])

  useSSE(storeId && tableId ? `/api/stores/${storeId}/tables/${tableId}/sse/orders` : '', handleSSE)

  if (loading) return <LoadingSpinner />

  return (
    <div style={{ padding: 16, maxWidth: 600, margin: '0 auto' }}>
      <h1>주문 내역</h1>
      {orders.length === 0 ? <p>주문 내역이 없습니다.</p> : orders.map(order => (
        <div key={order.id} style={{ border: '1px solid #e2e8f0', borderRadius: 8, padding: 16, marginBottom: 12 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
            <span style={{ fontSize: 14, color: '#718096' }}>{new Date(order.createdAt).toLocaleTimeString()}</span>
            <span style={{ padding: '2px 10px', borderRadius: 12, background: STATUS_COLOR[order.status], color: '#fff', fontSize: 13 }}>{STATUS_LABEL[order.status]}</span>
          </div>
          {order.items.map(item => (
            <div key={item.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0' }}>
              <span>{item.menuName} x{item.quantity}</span>
              <span>{(item.unitPrice * item.quantity).toLocaleString()}원</span>
            </div>
          ))}
          <div style={{ borderTop: '1px solid #e2e8f0', marginTop: 8, paddingTop: 8, fontWeight: 'bold', textAlign: 'right' }}>{order.totalAmount.toLocaleString()}원</div>
        </div>
      ))}
    </div>
  )
}
