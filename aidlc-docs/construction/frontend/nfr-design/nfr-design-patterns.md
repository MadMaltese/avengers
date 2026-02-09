# Frontend - NFR Design 패턴

## 보안 패턴

### Axios Interceptor
```
apiClient (axios instance)
  |
  +-- Request Interceptor: Authorization: Bearer {token} 자동 첨부
  +-- Response Interceptor: 401 → authStore.logout() → 로그인 리다이렉트
```

### Route Guard
```
ProtectedRoute 컴포넌트
  |
  +-- TABLE 역할: /customer/* 접근 허용
  +-- ADMIN 역할: /admin/* 접근 허용
  +-- 미인증: 로그인 화면 리다이렉트
```

## 성능 패턴

### 상태 관리 (Zustand)
- cartStore, authStore: localStorage persist (새로고침 유지)
- menuStore, orderStore, adminOrderStore: 메모리만 (API 재호출)
- 선택적 구독: 필요한 상태만 구독하여 불필요한 리렌더링 방지

### SSE 재연결 패턴
```
useSSE Hook
  |
  +-- EventSource 연결
  +-- onmessage → store 업데이트
  +-- onerror → 3초 후 자동 재연결 시도
  +-- 컴포넌트 unmount → 연결 해제
```

## UX 패턴

### 낙관적 업데이트 (Optimistic Update)
- 장바구니 조작: 즉시 UI 반영 (서버 호출 없음, 로컬 상태)
- 주문 상태 변경: SSE로 확인 후 최종 반영

### 로딩/에러 상태 관리
```
각 API 호출 시:
  +-- isLoading: true → 로딩 인디케이터 표시
  +-- 성공 → 데이터 업데이트
  +-- 실패 → 에러 메시지 표시 (toast/alert)
```

### 확인 팝업 패턴
- 파괴적 작업 (주문 삭제, 이용 완료): 확인 모달 필수
- 확인 → 실행, 취소 → 복귀
