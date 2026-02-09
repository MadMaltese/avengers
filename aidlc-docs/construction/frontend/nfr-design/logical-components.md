# Frontend - 논리적 컴포넌트

## 컴포넌트 구성

```
React Application (TypeScript, Vite)
  |
  +-- API Layer
  |     +-- apiClient (axios + interceptors)
  |     +-- authApi, menuApi, orderApi, tableApi
  |
  +-- Store Layer (Zustand)
  |     +-- authStore (persist: localStorage)
  |     +-- cartStore (persist: localStorage)
  |     +-- menuStore
  |     +-- orderStore
  |     +-- adminOrderStore
  |
  +-- Hook Layer
  |     +-- useSSE (SSE 연결 + 자동 재연결)
  |     +-- useAuth (인증 상태 편의 Hook)
  |
  +-- Page Layer
  |     +-- Customer: MenuPage, CartPage, OrderPage, SetupPage
  |     +-- Admin: LoginPage, DashboardPage, MenuManagementPage
  |
  +-- Component Layer (공통)
  |     +-- ProtectedRoute (Route Guard)
  |     +-- LoadingSpinner
  |     +-- ErrorMessage
  |     +-- ConfirmModal
  |
  +-- Router (React Router 6)
        +-- / → MenuPage
        +-- /customer/cart → CartPage
        +-- /customer/orders → OrderPage
        +-- /customer/setup → SetupPage
        +-- /admin/login → LoginPage
        +-- /admin/dashboard → DashboardPage
        +-- /admin/menus → MenuManagementPage
```
