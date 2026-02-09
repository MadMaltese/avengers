import { useEffect, useState } from 'react'
import { useAuthStore } from '../../stores/authStore'
import { useMenuStore } from '../../stores/menuStore'
import { useCartStore } from '../../stores/cartStore'
import { LoadingSpinner } from '../../components/Common'
import { useNavigate } from 'react-router-dom'

export default function MenuPage() {
  const { storeId } = useAuthStore()
  const { menus, categories, loading, fetchMenus, fetchCategories } = useMenuStore()
  const addItem = useCartStore(s => s.addItem)
  const totalItems = useCartStore(s => s.getTotalItems)
  const [selectedCategory, setSelectedCategory] = useState<number | undefined>()
  const navigate = useNavigate()

  useEffect(() => {
    if (storeId) { fetchCategories(storeId); fetchMenus(storeId) }
  }, [storeId, fetchCategories, fetchMenus])

  const handleCategoryClick = (catId?: number) => {
    setSelectedCategory(catId)
    if (storeId) fetchMenus(storeId, catId)
  }

  if (loading) return <LoadingSpinner />

  return (
    <div style={{ padding: 16 }}>
      <div style={{ display: 'flex', gap: 8, overflowX: 'auto', marginBottom: 16 }}>
        <button onClick={() => handleCategoryClick()} style={{ padding: '10px 20px', minHeight: 44, background: !selectedCategory ? '#3182ce' : '#e2e8f0', color: !selectedCategory ? '#fff' : '#000', border: 'none', borderRadius: 20 }}>전체</button>
        {categories.map(c => (
          <button key={c.id} onClick={() => handleCategoryClick(c.id)} style={{ padding: '10px 20px', minHeight: 44, background: selectedCategory === c.id ? '#3182ce' : '#e2e8f0', color: selectedCategory === c.id ? '#fff' : '#000', border: 'none', borderRadius: 20, whiteSpace: 'nowrap' }}>{c.name}</button>
        ))}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 16 }}>
        {menus.map(m => (
          <div key={m.id} style={{ border: '1px solid #e2e8f0', borderRadius: 8, padding: 16 }}>
            <h3 style={{ margin: '0 0 8px' }}>{m.name}</h3>
            <p style={{ color: '#718096', margin: '0 0 8px', fontSize: 14 }}>{m.description}</p>
            <p style={{ fontWeight: 'bold', margin: '0 0 12px' }}>{m.price.toLocaleString()}원</p>
            <button onClick={() => addItem({ menuId: m.id, menuName: m.name, price: m.price })} style={{ width: '100%', padding: 10, minHeight: 44, background: '#38a169', color: '#fff', border: 'none', borderRadius: 4 }}>담기</button>
          </div>
        ))}
      </div>
      {totalItems() > 0 && (
        <button onClick={() => navigate('/customer/cart')} style={{ position: 'fixed', bottom: 20, right: 20, padding: '14px 24px', minHeight: 44, background: '#3182ce', color: '#fff', border: 'none', borderRadius: 24, fontSize: 16 }}>
          장바구니 ({totalItems()})
        </button>
      )}
    </div>
  )
}
