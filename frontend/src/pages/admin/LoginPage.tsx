import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { authApi } from '../../api'
import { useAuthStore } from '../../stores/authStore'

export default function LoginPage() {
  const [storeCode, setStoreCode] = useState('')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const setAuth = useAuthStore(s => s.setAuth)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    try {
      const { data } = await authApi.adminLogin({ storeCode, username, password })
      setAuth(data.token, data.storeId, undefined, undefined, 'ADMIN')
      navigate('/admin/dashboard')
    } catch { setError('로그인 실패. 정보를 확인해주세요.') }
  }

  return (
    <div style={{ maxWidth: 400, margin: '80px auto', padding: 24 }}>
      <h1>관리자 로그인</h1>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        <input placeholder="매장 코드" value={storeCode} onChange={e => setStoreCode(e.target.value)} required style={{ padding: 12, fontSize: 16 }} />
        <input placeholder="아이디" value={username} onChange={e => setUsername(e.target.value)} required style={{ padding: 12, fontSize: 16 }} />
        <input placeholder="비밀번호" type="password" value={password} onChange={e => setPassword(e.target.value)} required style={{ padding: 12, fontSize: 16 }} />
        {error && <p style={{ color: '#e53e3e' }}>{error}</p>}
        <button type="submit" style={{ padding: 14, fontSize: 16, background: '#3182ce', color: '#fff', border: 'none', borderRadius: 4, minHeight: 44 }}>로그인</button>
      </form>
    </div>
  )
}
