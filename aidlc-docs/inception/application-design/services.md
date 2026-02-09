# 테이블오더 서비스 - 서비스 레이어 설계

## 아키텍처 패턴
- **Backend**: Layered Architecture (Controller → Service → Repository)
- **Frontend**: 단일 React 앱 + Zustand 상태 관리 + React Router 라우팅 분리

---

## Backend 서비스 레이어

### AuthService
- **역할**: 인증/인가 오케스트레이션
- **의존**: AdminRepository, TableRepository, JwtUtil
- **오케스트레이션**: 자격 증명 검증 → JWT 발급 → 로그인 시도 제한 관리

### MenuService
- **역할**: 메뉴 비즈니스 로직 오케스트레이션
- **의존**: MenuRepository, CategoryRepository
- **오케스트레이션**: 메뉴 CRUD → 검증 → 순서 관리

### OrderService
- **역할**: 주문 비즈니스 로직 오케스트레이션
- **의존**: OrderRepository, OrderItemRepository, SSEService
- **오케스트레이션**: 주문 생성 → 저장 → SSE 이벤트 발행

### TableService
- **역할**: 테이블/세션 관리 오케스트레이션
- **의존**: TableRepository, OrderRepository, OrderHistoryRepository, SSEService
- **오케스트레이션**: 세션 관리 → 이용 완료 시 주문 이력 이동 → SSE 이벤트 발행

### SSEService
- **역할**: 실시간 이벤트 브로드캐스트
- **의존**: SseEmitter 관리 (in-memory)
- **오케스트레이션**: 구독 관리 → 이벤트 수신 → 대상 클라이언트에 전송

---

## Frontend 서비스 레이어

### API Service (axios 기반)
- **역할**: Backend REST API 호출 추상화
- **구성**: apiClient (axios instance) + interceptor (JWT 토큰 자동 첨부, 401 처리)

### Zustand Stores
| Store | 역할 | Persist |
|-------|------|---------|
| authStore | 인증 상태, 토큰, 사용자 정보 | localStorage |
| menuStore | 메뉴/카테고리 데이터 | No |
| cartStore | 장바구니 항목, 수량, 총액 | localStorage |
| orderStore | 주문 내역, 주문 상태 | No |
| adminOrderStore | 관리자 주문 대시보드 데이터 | No |

### 라우팅 구조
```
/ → 고객 메뉴 화면 (기본)
/customer/cart → 장바구니
/customer/orders → 주문 내역
/customer/setup → 테이블 초기 설정 (관리자 1회)
/admin/login → 관리자 로그인
/admin/dashboard → 주문 대시보드
/admin/menus → 메뉴 관리
```
