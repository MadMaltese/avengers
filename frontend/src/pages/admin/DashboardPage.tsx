import { useEffect, useCallback, useState } from 'react'
import { useAuthStore } from '../../stores/authStore'
import { useAdminOrderStore } from '../../stores/adminOrderStore'
import { useSSE } from '../../hooks/useSSE'
import { orderApi, tableApi } from '../../api'
import { ConfirmModal, LoadingSpinner } from '../../components/Common'
import type { OrderResponse, OrderStatus } from '../../types'

const STATUS_LABEL: Record<string, string> = { PENDING: '대기중', PREPARING: '준비중', COMPLETED: '완료' }
const STATUS_COLOR: Record<string, string> = { PENDING: '#ed8936', PREPARING: '#3182ce', COMPLETED: '#38a169' }
const NEXT_STATUS: Record<string, OrderStatus> = { PENDING: 'PREPARING', PREPARING: 'COMPLETED' }

export default function DashboardPage() {
  const { storeId } = useAuthStore()
  const { orders, tables, loading, fetchOrders, fetchTables, updateOrderInList, addOrder, removeOrderFromList } = useAdminOrderStore()
  const [confirmAction, setConfirmAction] = useState<{ message: string; action: () => void } | null>(null)

  useEffect(() => {
    if (storeId) { fetchOrders(storeId); fetchTables(storeId) }
  }, [storeId, fetchOrders, fetchTables])

  const handleSSE = useCallback((event: MessageEvent) => {
    try {
      const order: OrderResponse = JSON.parse(event.data)
      const exists = orders.find(o => o.id === order.id)
      if (exists) updateOrderInList(order)
      else addOrder(order)
    } catch { /* ignore */ }
  }, [orders, updateOrderInList, addOrder])

  useSSE(storeId ? `/api/stores/${storeId}/sse/orders` : '', handleSSE)

  const handleStatusChange = async (orderId: string, status: OrderStatus) => {
    const { data } = await orderApi.updateStatus(orderId, status)
    updateOrderInList(data)
  }

  const handleDelete = async (orderId: string) => {
    await orderApi.remove(orderId)
    removeOrderFromList(orderId)
    setConfirmAction(null)
  }

  const handleCompleteTable = async (tableId: number) => {
    await tableApi.complete(storeId!, tableId)
    fetchTables(storeId!)
    fetchOrders(storeId!)
    setConfirmAction(null)
  }

  if (loading) return <LoadingSpinner />

  const activeOrders = orders.filter(o => o.status !== 'COMPLETED')
  const groupedByTable = activeOrders.reduce<Record<number, OrderResponse[]>>((acc, o) => {
    ;(acc[o.tableNo] ??= []).push(o)
    return acc
  }, {})

  return (
    <div style={{ padding: 16 }}>
      <h1>주문 대시보드</h1>
      <div style={{ display: 'flex', gap: 8, marginBottom: 16, flexWrap: 'wrap' }}>
        {tables.map(t => (
          <div key={t.id} style={{ padding: '8px 14px', borderRadius: 8, background: t.status === 'OCCUPIED' ? '#fed7d7' : '#c6f6d5', fontSize: 14 }}>
            {t.tableNo}번 {t.status === 'OCCUPIED' ? '사용중' : '빈 테이블'}
            {t.status === 'OCCUPIED' && (
              <button onClick={() => setConfirmAction({ message: `${t.tableNo}번 테이블을 퇴석 처리하시겠습니까?`, action: () => handleCompleteTable(t.id) })} style={{ marginLeft: 8, fontSize: 12, background: 'none', border: '1px solid #e53e3e', color: '#e53e3e', borderRadius: 4, padding: '2px 6px', cursor: 'pointer' }}>퇴석</button>
            )}
          </div>
        ))}
      </div>
      {Object.keys(groupedByTable).length === 0 ? <p>활성 주문이 없습니다.</p> : Object.entries(groupedByTable).map(([tableNo, tableOrders]) => (
        <div key={tableNo} style={{ marginBottom: 24 }}>
          <h2 style={{ fontSize: 18 }}>{tableNo}번 테이블</h2>
          {tableOrders.map(order => (
            <div key={order.id} style={{ border: '1px solid #e2e8f0', borderRadius: 8, padding: 16, marginBottom: 8 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                <span style={{ fontSize: 13, color: '#718096' }}>{new Date(order.createdAt).toLocaleTimeString()}</span>
                <span style={{ padding: '2px 10px', borderRadius: 12, background: STATUS_COLOR[order.status], color: '#fff', fontSize: 13 }}>{STATUS_LABEL[order.status]}</span>
              </div>
              {order.items.map(item => (
                <div key={item.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '2px 0', fontSize: 14 }}>
                  <span>{item.menuName} x{item.quantity}</span>
                  <span>{(item.unitPrice * item.quantity).toLocaleString()}원</span>
                </div>
              ))}
              <div style={{ borderTop: '1px solid #e2e8f0', marginTop: 8, paddingTop: 8, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <strong>{order.totalAmount.toLocaleString()}원</strong>
                <div style={{ display: 'flex', gap: 6 }}>
                  {NEXT_STATUS[order.status] && (
                    <button onClick={() => handleStatusChange(order.id, NEXT_STATUS[order.status])} style={{ padding: '6px 12px', minHeight: 36, background: '#3182ce', color: '#fff', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
                      {NEXT_STATUS[order.status] === 'PREPARING' ? '준비 시작' : '완료'}
                    </button>
                  )}
                  <button onClick={() => setConfirmAction({ message: '이 주문을 취소하시겠습니까?', action: () => handleDelete(order.id) })} style={{ padding: '6px 12px', minHeight: 36, background: '#e53e3e', color: '#fff', border: 'none', borderRadius: 4, cursor: 'pointer' }}>취소</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      ))}
      {confirmAction && <ConfirmModal message={confirmAction.message} onConfirm={confirmAction.action} onCancel={() => setConfirmAction(null)} />}
    </div>
  )
}
