# TDD Code Generation Plan - frontend

## Unit Context
- **Workspace Root**: /Users/kirby/Desktop/project/ai-dlc/avengers
- **Project Type**: Greenfield (multi-unit)
- **Code Location**: frontend/
- **Stories**: US-C01~C16, US-A01~A16 (32개)

## Plan Step 0: 프로젝트 구조 + Skeleton 생성
- [ ] Vite + React + TypeScript 프로젝트 구조 생성
- [ ] package.json (의존성 정의)
- [ ] tsconfig.json, vite.config.ts
- [ ] 디렉토리 구조 (api/, components/, hooks/, pages/, stores/, types/)
- [ ] TypeScript 타입 정의 (types/)
- [ ] API client skeleton (api/)
- [ ] Store skeleton (stores/)
- [ ] 컴파일 확인

## Plan Step 1: Store Layer (TDD)
- [ ] cartStore - RED-GREEN-REFACTOR
  - [ ] RED: TC-FE-001~007
  - [ ] GREEN: 최소 구현
  - [ ] REFACTOR: 정리
  - [ ] VERIFY: 테스트 통과
- [ ] authStore - RED-GREEN-REFACTOR
  - [ ] RED: TC-FE-008~009
  - [ ] GREEN: 최소 구현
  - [ ] REFACTOR: 정리
  - [ ] VERIFY: 테스트 통과

## Plan Step 2: Hook Layer (TDD)
- [ ] useSSE - RED-GREEN-REFACTOR
  - [ ] RED: TC-FE-010~011
  - [ ] GREEN: 최소 구현
  - [ ] REFACTOR: 정리
  - [ ] VERIFY: 테스트 통과

## Plan Step 3: Component Layer (TDD)
- [ ] ProtectedRoute - RED-GREEN-REFACTOR
  - [ ] RED: TC-FE-012~013
  - [ ] GREEN: 최소 구현
  - [ ] REFACTOR: 정리
  - [ ] VERIFY: 테스트 통과
- [ ] 공통 컴포넌트 (ConfirmModal, LoadingSpinner, ErrorMessage)

## Plan Step 4: API Layer
- [ ] apiClient (axios instance + interceptors)
- [ ] authApi, menuApi, orderApi, tableApi

## Plan Step 5: Page Layer - Customer
- [ ] SetupPage (테이블 초기 설정)
- [ ] MenuPage (메뉴 조회, 카테고리 탐색, 장바구니 추가)
- [ ] CartPage (장바구니 관리, 주문 확인/확정)
- [ ] OrderPage (주문 내역, 실시간 상태)

## Plan Step 6: Page Layer - Admin
- [ ] LoginPage (관리자 로그인)
- [ ] DashboardPage (주문 대시보드, SSE, 상태 변경, 테이블 관리)
- [ ] MenuManagementPage (메뉴 CRUD, 순서 조정)

## Plan Step 7: Router + App
- [ ] App.tsx (React Router 설정)
- [ ] main.tsx (엔트리 포인트)

## Plan Step 8: 통합 검증
- [ ] 전체 테스트 실행
- [ ] 타입 체크 (tsc --noEmit)
- [ ] 빌드 확인 (vite build)
