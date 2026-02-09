# NFR Requirements Plan - All Units

## 개요
3개 유닛의 비기능 요구사항 및 기술 스택 세부 결정

## 이미 결정된 사항
- 기술 스택: React + Spring Boot + PostgreSQL
- 배포: 로컬/온프레미스
- 규모: 소규모 단일 매장 (20테이블 이하)
- 인증: JWT 16h, bcrypt, 10회/30분 잠금
- 실시간: SSE

---

## 질문 (Questions)

### Question 1
Backend 빌드 도구는 어떤 것을 사용하시겠습니까?

A) Gradle (Groovy DSL)
B) Gradle (Kotlin DSL)
C) Maven
D) Other (please describe after [Answer]: tag below)

[Answer]: A

### Question 2
Java 버전은 어떤 것을 사용하시겠습니까?

A) Java 17 (LTS)
B) Java 21 (LTS)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

### Question 3
Spring Boot 버전은 어떤 것을 사용하시겠습니까?

A) Spring Boot 3.2.x (안정)
B) Spring Boot 3.4.x (최신)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

### Question 4
API 응답 시간 목표는 어느 정도로 설정하시겠습니까?

A) 500ms 이내 (일반적인 웹 서비스)
B) 200ms 이내 (빠른 응답)
C) 특별한 목표 없음 (합리적인 수준이면 OK)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

---

## 실행 계획 (Execution Plan)

### 전체 유닛 공통
- [x] NFR 요구사항 정의 (성능, 보안, 가용성, 유지보수성)
- [x] 기술 스택 세부 결정 (버전, 라이브러리)

### database 유닛
- [x] DB 성능 관련 NFR (인덱스 전략, 커넥션 풀)

### backend 유닛
- [x] 서버 성능/보안 NFR
- [x] 기술 스택 상세 (Spring Boot 버전, 의존성)

### frontend 유닛
- [x] 클라이언트 성능/UX NFR
- [x] 기술 스택 상세 (React 버전, 빌드 도구)
