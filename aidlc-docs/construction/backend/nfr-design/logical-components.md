# Backend - 논리적 컴포넌트

## 컴포넌트 구성

```
Spring Boot Application (Java 21, Spring Boot 3.4.x)
  |
  +-- Security Layer
  |     +-- SecurityConfig (필터 체인, CORS)
  |     +-- JwtAuthenticationFilter (토큰 검증)
  |     +-- JwtUtil (토큰 생성/파싱)
  |
  +-- Controller Layer
  |     +-- AuthController
  |     +-- MenuController
  |     +-- OrderController
  |     +-- TableController
  |     +-- SSEController
  |
  +-- Service Layer
  |     +-- AuthService
  |     +-- MenuService
  |     +-- OrderService (@Transactional)
  |     +-- TableService (@Transactional)
  |     +-- SSEService (ConcurrentHashMap emitter 관리)
  |
  +-- Repository Layer (Spring Data JPA)
  |     +-- StoreRepository
  |     +-- AdminRepository
  |     +-- TableInfoRepository
  |     +-- CategoryRepository
  |     +-- MenuRepository
  |     +-- OrderRepository
  |     +-- OrderItemRepository
  |     +-- OrderHistoryRepository
  |     +-- OrderHistoryItemRepository
  |
  +-- Cross-cutting
        +-- GlobalExceptionHandler (@RestControllerAdvice)
        +-- Bean Validation (Request DTO)
        +-- Swagger/OpenAPI (springdoc)
        +-- Logback (SLF4J)
```
