# 테이블오더 서비스 - Unit of Work 의존성

## 의존성 매트릭스

| 유닛 | database | backend | frontend |
|------|----------|---------|----------|
| **database** | - | 없음 | 없음 |
| **backend** | DB 스키마 의존 | - | 없음 |
| **frontend** | 없음 | REST API / SSE 의존 | - |

## 의존성 다이어그램

```
+------------+
|  database  |  (독립, 1순위)
+------------+
      |
      | JPA/JDBC
      v
+------------+     REST API / SSE     +------------+
|  backend   | <--------------------- |  frontend  |
+------------+                        +------------+
```

## 개발 순서 전략

```
Phase 1 (동시 시작):
  - database: 스키마 DDL + 시드 데이터
  - backend: 프로젝트 셋업 + Entity/DTO 정의 + API 스펙 문서화
  - frontend: 프로젝트 셋업 + 타입 정의 + UI 컴포넌트 (mock 데이터)

Phase 2 (API 연동):
  - backend: Service/Repository 구현 + API 엔드포인트
  - frontend: API 연동 + Store 연결

Phase 3 (실시간 기능):
  - backend: SSE 구현
  - frontend: SSE Hook 연동
```

## 통합 포인트

| 통합 포인트 | 유닛 간 | 계약 |
|-------------|---------|------|
| DB 연결 | database ↔ backend | JPA Entity ↔ DDL 스키마 일치 |
| REST API | backend ↔ frontend | API 엔드포인트 + Request/Response DTO |
| SSE | backend ↔ frontend | SSE 이벤트 타입 + 페이로드 형식 |
| 인증 | backend ↔ frontend | JWT 토큰 형식 + Header 규약 |
