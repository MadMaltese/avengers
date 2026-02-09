import { useEffect, useState } from 'react'
import { useAuthStore } from '../../stores/authStore'
import { useMenuStore } from '../../stores/menuStore'
import { menuApi } from '../../api'
import { ConfirmModal, LoadingSpinner } from '../../components/Common'
import type { MenuCreateRequest, MenuUpdateRequest } from '../../types'

export default function MenuManagementPage() {
  const { storeId } = useAuthStore()
  const { menus, categories, loading, fetchMenus, fetchCategories } = useMenuStore()
  const [editId, setEditId] = useState<number | null>(null)
  const [form, setForm] = useState<MenuCreateRequest>({ name: '', price: 0, description: '', categoryId: 0 })
  const [deleteId, setDeleteId] = useState<number | null>(null)
  const [error, setError] = useState('')

  useEffect(() => {
    if (storeId) { fetchMenus(storeId); fetchCategories(storeId) }
  }, [storeId, fetchMenus, fetchCategories])

  const resetForm = () => { setForm({ name: '', price: 0, description: '', categoryId: categories[0]?.id ?? 0 }); setEditId(null); setError('') }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    try {
      if (editId) {
        const update: MenuUpdateRequest = { name: form.name, price: form.price, description: form.description, categoryId: form.categoryId }
        await menuApi.update(editId, update)
      } else {
        await menuApi.create(storeId!, form)
      }
      resetForm()
      fetchMenus(storeId!)
    } catch { setError('저장 실패') }
  }

  const handleEdit = (m: { id: number; name: string; price: number; description: string | null; categoryId: number }) => {
    setEditId(m.id)
    setForm({ name: m.name, price: m.price, description: m.description ?? '', categoryId: m.categoryId })
  }

  const handleDelete = async () => {
    if (!deleteId) return
    await menuApi.remove(deleteId)
    setDeleteId(null)
    fetchMenus(storeId!)
  }

  if (loading) return <LoadingSpinner />

  return (
    <div style={{ padding: 16, maxWidth: 800, margin: '0 auto' }}>
      <h1>메뉴 관리</h1>
      <form onSubmit={handleSubmit} style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 24, padding: 16, background: '#f7fafc', borderRadius: 8 }}>
        <select value={form.categoryId} onChange={e => setForm(f => ({ ...f, categoryId: Number(e.target.value) }))} required style={{ padding: 10, minHeight: 44 }}>
          <option value={0} disabled>카테고리</option>
          {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
        </select>
        <input placeholder="메뉴명" value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} required style={{ padding: 10, flex: 1, minWidth: 120 }} />
        <input placeholder="가격" type="number" value={form.price || ''} onChange={e => setForm(f => ({ ...f, price: Number(e.target.value) }))} required style={{ padding: 10, width: 100 }} />
        <input placeholder="설명" value={form.description} onChange={e => setForm(f => ({ ...f, description: e.target.value }))} style={{ padding: 10, flex: 1, minWidth: 120 }} />
        <button type="submit" style={{ padding: '10px 20px', minHeight: 44, background: '#3182ce', color: '#fff', border: 'none', borderRadius: 4 }}>{editId ? '수정' : '추가'}</button>
        {editId && <button type="button" onClick={resetForm} style={{ padding: '10px 20px', minHeight: 44 }}>취소</button>}
        {error && <p style={{ width: '100%', color: '#e53e3e', margin: 0 }}>{error}</p>}
      </form>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid #e2e8f0', textAlign: 'left' }}>
            <th style={{ padding: 8 }}>카테고리</th><th style={{ padding: 8 }}>메뉴명</th><th style={{ padding: 8 }}>가격</th><th style={{ padding: 8 }}>설명</th><th style={{ padding: 8 }}>관리</th>
          </tr>
        </thead>
        <tbody>
          {menus.map(m => (
            <tr key={m.id} style={{ borderBottom: '1px solid #e2e8f0' }}>
              <td style={{ padding: 8 }}>{m.categoryName}</td>
              <td style={{ padding: 8 }}>{m.name}</td>
              <td style={{ padding: 8 }}>{m.price.toLocaleString()}원</td>
              <td style={{ padding: 8, color: '#718096' }}>{m.description}</td>
              <td style={{ padding: 8 }}>
                <button onClick={() => handleEdit(m)} style={{ marginRight: 4, padding: '4px 8px', minHeight: 36 }}>수정</button>
                <button onClick={() => setDeleteId(m.id)} style={{ padding: '4px 8px', minHeight: 36, color: '#e53e3e' }}>삭제</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {deleteId && <ConfirmModal message="이 메뉴를 삭제하시겠습니까?" onConfirm={handleDelete} onCancel={() => setDeleteId(null)} />}
    </div>
  )
}
