# Unit Test Execution

## TDD 상태
TDD 방식으로 코드 생성됨. 단위 테스트는 Code Generation 단계에서 이미 실행 및 통과.

## Backend Unit Tests

### 실행
```bash
cd backend
./gradlew test
```

### 테스트 현황
| 파일 | 테스트 수 | 커버리지 |
|------|-----------|----------|
| AuthServiceTest | 6 | TC-BE-001~006 |
| OrderServiceTest | 7 | TC-BE-014~020 |
| **합계** | **13** | |

### 테스트 리포트
- `backend/build/reports/tests/test/index.html`

## Frontend Unit Tests

### 실행
```bash
cd frontend
npx vitest run
```

### 테스트 현황
| 파일 | 테스트 수 | 커버리지 |
|------|-----------|----------|
| cartStore.test.ts | 7 | TC-FE-001~007 |
| authStore.test.ts | 2 | TC-FE-008~009 |
| useSSE.test.ts | 2 | TC-FE-010~011 |
| ProtectedRoute.test.tsx | 2 | TC-FE-012~013 |
| **합계** | **13** | |

## Database Schema Tests

### 실행
```bash
psql -d tableorder -f database/tests/validate_schema.sql
```

### 검증 항목
- TC-DB-001~008: 테이블 구조, FK, UNIQUE, CHECK 제약조건, CASCADE/RESTRICT, 시드 데이터

## 전체 테스트 실행 (한번에)
```bash
# Backend
cd backend && ./gradlew test

# Frontend
cd frontend && npx vitest run
```

- **기대 결과**: Backend 13 tests passed, Frontend 13 tests passed
