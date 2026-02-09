import { describe, it, expect, beforeEach } from 'vitest'
import { useCartStore } from '../stores/cartStore'

const MENU_A = { menuId: 1, menuName: '김치찌개', price: 8000 }
const MENU_B = { menuId: 2, menuName: '된장찌개', price: 7000 }

describe('cartStore', () => {
  beforeEach(() => useCartStore.setState({ items: [] }))

  // TC-FE-001: 장바구니에 메뉴 추가
  it('adds item to cart', () => {
    useCartStore.getState().addItem(MENU_A)
    expect(useCartStore.getState().items).toHaveLength(1)
    expect(useCartStore.getState().items[0]).toMatchObject({ ...MENU_A, quantity: 1 })
  })

  // TC-FE-002: 동일 메뉴 추가 시 수량 증가
  it('increments quantity for duplicate item', () => {
    useCartStore.getState().addItem(MENU_A)
    useCartStore.getState().addItem(MENU_A)
    expect(useCartStore.getState().items).toHaveLength(1)
    expect(useCartStore.getState().items[0].quantity).toBe(2)
  })

  // TC-FE-003: 장바구니 항목 제거
  it('removes item from cart', () => {
    useCartStore.getState().addItem(MENU_A)
    useCartStore.getState().addItem(MENU_B)
    useCartStore.getState().removeItem(1)
    expect(useCartStore.getState().items).toHaveLength(1)
    expect(useCartStore.getState().items[0].menuId).toBe(2)
  })

  // TC-FE-004: 수량 변경
  it('updates quantity', () => {
    useCartStore.getState().addItem(MENU_A)
    useCartStore.getState().updateQuantity(1, 5)
    expect(useCartStore.getState().items[0].quantity).toBe(5)
  })

  // TC-FE-005: 수량 0 이하 시 항목 제거
  it('removes item when quantity <= 0', () => {
    useCartStore.getState().addItem(MENU_A)
    useCartStore.getState().updateQuantity(1, 0)
    expect(useCartStore.getState().items).toHaveLength(0)
  })

  // TC-FE-006: 장바구니 비우기
  it('clears cart', () => {
    useCartStore.getState().addItem(MENU_A)
    useCartStore.getState().addItem(MENU_B)
    useCartStore.getState().clearCart()
    expect(useCartStore.getState().items).toHaveLength(0)
  })

  // TC-FE-007: 총 금액 계산
  it('calculates total price', () => {
    useCartStore.getState().addItem(MENU_A)
    useCartStore.getState().addItem(MENU_B)
    useCartStore.getState().updateQuantity(1, 2)
    expect(useCartStore.getState().getTotalPrice()).toBe(8000 * 2 + 7000)
  })
})
