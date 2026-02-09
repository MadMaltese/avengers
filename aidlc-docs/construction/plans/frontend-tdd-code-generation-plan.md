# TDD Code Generation Plan - frontend

## Unit Context
- **Workspace Root**: /Users/kirby/Desktop/project/ai-dlc/avengers
- **Project Type**: Greenfield (multi-unit)
- **Code Location**: frontend/
- **Stories**: US-C01~C16, US-A01~A16 (32개)

## Plan Step 0: 프로젝트 구조 + Skeleton 생성
- [x] Vite + React + TypeScript 프로젝트 구조 생성
- [x] package.json (의존성 정의)
- [x] tsconfig.json, vite.config.ts
- [x] 디렉토리 구조 (api/, components/, hooks/, pages/, stores/, types/)
- [x] TypeScript 타입 정의 (types/)
- [x] API client skeleton (api/)
- [x] Store skeleton (stores/)
- [x] 컴파일 확인

## Plan Step 1: Store Layer (TDD)
- [x] cartStore - RED-GREEN-REFACTOR
  - [x] RED: TC-FE-001~007
  - [x] GREEN: 최소 구현
  - [x] REFACTOR: 정리
  - [x] VERIFY: 테스트 통과
- [x] authStore - RED-GREEN-REFACTOR
  - [x] RED: TC-FE-008~009
  - [x] GREEN: 최소 구현
  - [x] REFACTOR: 정리
  - [x] VERIFY: 테스트 통과

## Plan Step 2: Hook Layer (TDD)
- [x] useSSE - RED-GREEN-REFACTOR
  - [x] RED: TC-FE-010~011
  - [x] GREEN: 최소 구현
  - [x] REFACTOR: 정리
  - [x] VERIFY: 테스트 통과

## Plan Step 3: Component Layer (TDD)
- [x] ProtectedRoute - RED-GREEN-REFACTOR
  - [x] RED: TC-FE-012~013
  - [x] GREEN: 최소 구현
  - [x] REFACTOR: 정리
  - [x] VERIFY: 테스트 통과
- [x] 공통 컴포넌트 (ConfirmModal, LoadingSpinner, ErrorMessage)

## Plan Step 4: API Layer
- [x] apiClient (axios instance + interceptors)
- [x] authApi, menuApi, orderApi, tableApi

## Plan Step 5: Page Layer - Customer
- [x] SetupPage (테이블 초기 설정)
- [x] MenuPage (메뉴 조회, 카테고리 탐색, 장바구니 추가)
- [x] CartPage (장바구니 관리, 주문 확인/확정)
- [x] OrderPage (주문 내역, 실시간 상태)

## Plan Step 6: Page Layer - Admin
- [x] LoginPage (관리자 로그인)
- [x] DashboardPage (주문 대시보드, SSE, 상태 변경, 테이블 관리)
- [x] MenuManagementPage (메뉴 CRUD, 순서 조정)

## Plan Step 7: Router + App
- [x] App.tsx (React Router 설정)
- [x] main.tsx (엔트리 포인트)

## Plan Step 8: 통합 검증
- [x] 전체 테스트 실행
- [x] 타입 체크 (tsc --noEmit)
- [x] 빌드 확인 (vite build)
