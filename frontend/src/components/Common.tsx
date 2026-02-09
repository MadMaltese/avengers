export function ConfirmModal({ message, onConfirm, onCancel }: { message: string; onConfirm: () => void; onCancel: () => void }) {
  return (
    <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
      <div style={{ background: '#fff', padding: 24, borderRadius: 8, minWidth: 300 }}>
        <p>{message}</p>
        <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end', marginTop: 16 }}>
          <button onClick={onCancel} style={{ padding: '8px 16px' }}>취소</button>
          <button onClick={onConfirm} style={{ padding: '8px 16px', background: '#e53e3e', color: '#fff', border: 'none', borderRadius: 4 }}>확인</button>
        </div>
      </div>
    </div>
  )
}

export function LoadingSpinner() {
  return <div style={{ display: 'flex', justifyContent: 'center', padding: 40 }}>로딩 중...</div>
}

export function ErrorMessage({ message }: { message: string }) {
  return <div style={{ color: '#e53e3e', padding: 16, textAlign: 'center' }}>{message}</div>
}
