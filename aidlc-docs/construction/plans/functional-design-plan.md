# Functional Design Plan - All Units (database / backend / frontend)

## 개요
3개 유닛의 비즈니스 로직, 도메인 모델, 비즈니스 규칙을 상세 설계

---

## 질문 (Questions)

### Question 1
주문 상태 전이 규칙은 어떻게 하시겠습니까?

A) 순방향만 허용 (대기중 → 준비중 → 완료, 역방향 불가)
B) 자유 전이 허용 (어떤 상태에서든 다른 상태로 변경 가능)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

### Question 2
주문 번호 생성 방식은 어떻게 하시겠습니까?

A) DB 자동 증가 ID (1, 2, 3...)
B) 날짜 기반 순번 (20260209-001, 20260209-002...)
C) UUID
D) Other (please describe after [Answer]: tag below)

[Answer]: C

### Question 3
테이블 세션 ID 생성 방식은 어떻게 하시겠습니까? (세션 시작 = 첫 주문 시점)

A) UUID 자동 생성 (첫 주문 시 새 세션 ID 발급)
B) 타임스탬프 기반 (테이블번호-yyyyMMddHHmmss)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

### Question 4
관리자 로그인 시도 제한은 어떤 수준으로 하시겠습니까?

A) 5회 실패 시 15분 잠금
B) 10회 실패 시 30분 잠금
C) Other (please describe after [Answer]: tag below)

[Answer]: B

---

## 실행 계획 (Execution Plan)

### database 유닛
- [x] 도메인 엔티티 정의 (ERD, 테이블 관계)
- [x] 비즈니스 규칙 (제약조건, 검증 규칙)

### backend 유닛
- [x] 비즈니스 로직 모델 (서비스별 상세 로직 흐름)
- [x] 비즈니스 규칙 (검증, 상태 전이, 인증 규칙)
- [x] 도메인 엔티티 (JPA Entity 상세)

### frontend 유닛
- [x] 비즈니스 로직 모델 (화면별 상태 흐름, 사용자 인터랙션)
- [x] 비즈니스 규칙 (클라이언트 검증, 장바구니 로직)
- [x] 도메인 엔티티 (TypeScript 타입 정의)
