# 테이블오더 서비스 - Unit of Work 정의

## 유닛 구성: 3개 유닛
## 개발 방식: 동시 개발 (API 스펙 합의 후 병렬 진행)

---

## Unit 1: DB Schema (database)

| 항목 | 내용 |
|------|------|
| **유닛명** | database |
| **기술** | PostgreSQL + DDL/Migration Scripts |
| **책임** | 데이터베이스 스키마 정의, 초기 데이터, 마이그레이션 |
| **개발 순서** | 1순위 (Backend/Frontend보다 먼저 또는 동시 시작) |

**범위:**
- Store, Admin, TableInfo, Category, Menu, Order, OrderItem, OrderHistory 테이블 DDL
- 인덱스, 제약조건, FK 관계 정의
- 초기 시드 데이터 (테스트용 매장/메뉴)

**코드 구조:**
```
database/
  migrations/
    V1__create_tables.sql
  seed/
    V99__seed_data.sql
  README.md
```

---

## Unit 2: Backend (backend)

| 항목 | 내용 |
|------|------|
| **유닛명** | backend |
| **기술** | Java + Spring Boot + JPA + PostgreSQL |
| **책임** | REST API, 비즈니스 로직, 인증, SSE |
| **개발 순서** | DB Schema와 동시 시작 |

**범위:**
- Layered Architecture (Controller → Service → Repository)
- AuthController/Service: 테이블 인증, 관리자 인증, JWT
- MenuController/Service: 메뉴 CRUD, 카테고리
- OrderController/Service: 주문 CRUD, 상태 변경
- TableController/Service: 테이블 설정, 세션 관리, 이용 완료
- SSEController/Service: 실시간 이벤트 브로드캐스트
- SecurityConfig: JWT 필터, CORS

**코드 구조:**
```
backend/
  src/main/java/com/tableorder/
    config/          (SecurityConfig, CorsConfig)
    controller/      (Auth, Menu, Order, Table, SSE)
    service/         (Auth, Menu, Order, Table, SSE)
    repository/      (JPA Repositories)
    entity/          (JPA Entities)
    dto/             (Request/Response DTOs)
    security/        (JwtUtil, JwtFilter)
    exception/       (GlobalExceptionHandler)
  src/main/resources/
    application.yml
  build.gradle
```

---

## Unit 3: Frontend (frontend)

| 항목 | 내용 |
|------|------|
| **유닛명** | frontend |
| **기술** | TypeScript + React + Zustand + React Router |
| **책임** | 고객 UI, 관리자 UI, 상태 관리, SSE 클라이언트 |
| **개발 순서** | Backend와 동시 시작 (API 스펙 합의 후) |

**범위:**
- 단일 React 앱, 라우팅으로 고객/관리자 분리
- 고객: 메뉴 조회, 장바구니, 주문, 주문 내역
- 관리자: 로그인, 대시보드, 테이블 관리, 메뉴 관리
- Zustand Store 5개 (auth, menu, cart, order, adminOrder)
- SSE Hook, API Service (axios)

**코드 구조:**
```
frontend/
  src/
    api/             (apiClient, endpoints)
    components/      (공통 UI 컴포넌트)
    hooks/           (useSSE, useAuth 등)
    pages/
      customer/      (MenuPage, CartPage, OrderPage, SetupPage)
      admin/         (LoginPage, DashboardPage, MenuManagementPage)
    stores/          (Zustand stores)
    types/           (TypeScript 타입 정의)
    App.tsx
    main.tsx
  package.json
  tsconfig.json
  vite.config.ts
```
