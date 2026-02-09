# Backend - NFR Design 패턴

## 보안 패턴

### JWT 인증 필터 체인
```
Request → JwtAuthenticationFilter → SecurityContext → Controller
  |
  +-- 토큰 없음/만료 → 401 Unauthorized
  +-- 역할 불일치 → 403 Forbidden
  +-- 유효 → SecurityContext에 AuthInfo 설정
```

### 역할 기반 접근 제어 (RBAC)
| 경로 패턴 | 허용 역할 |
|-----------|----------|
| POST /api/auth/** | 인증 불필요 |
| /api/menus (GET) | TABLE, ADMIN |
| /api/orders (고객) | TABLE |
| /api/orders (관리자) | ADMIN |
| /api/tables/** | ADMIN |
| /api/menus (CUD) | ADMIN |
| /api/sse/orders | ADMIN |
| /api/sse/orders/table/** | TABLE |

### 로그인 시도 제한 (Rate Limiting)
- In-memory 카운터 (Admin 엔티티의 failedAttempts, lockedUntil)
- 10회 실패 → 30분 잠금
- 성공 시 카운터 리셋

## 성능 패턴

### SSE 이벤트 브로드캐스트
```
OrderService → SSEService.broadcastOrderEvent()
  |
  +-- ConcurrentHashMap<storeId, List<SseEmitter>> (관리자)
  +-- ConcurrentHashMap<storeId:tableId, List<SseEmitter>> (고객)
  +-- 전송 실패 시 emitter 자동 제거
  +-- SseEmitter timeout: 16시간
```

### 에러 처리 패턴
```
@RestControllerAdvice GlobalExceptionHandler
  |
  +-- EntityNotFoundException → 404
  +-- IllegalStateException → 400 (상태 전이 오류)
  +-- ValidationException → 400 (입력 검증 오류)
  +-- AuthenticationException → 401
  +-- AccessDeniedException → 403
  +-- AccountLockedException → 423
  +-- Exception → 500

응답 형식: { "error": "에러 코드", "message": "설명" }
```

## 유지보수 패턴

### Layered Architecture 준수
```
Controller (HTTP 요청/응답 변환)
    |
Service (비즈니스 로직, @Transactional)
    |
Repository (데이터 접근, Spring Data JPA)
```

### DTO 패턴
- Request DTO: 입력 검증 (@Valid, @NotBlank, @Min 등)
- Response DTO: Entity 노출 방지, 필요한 필드만 반환
- Entity ↔ DTO 변환은 Service 레이어에서 수행
