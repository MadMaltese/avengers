# Application Design Plan

## 설계 범위
테이블오더 서비스의 고수준 컴포넌트 식별, 서비스 레이어 설계, 컴포넌트 간 의존성 정의

---

## 질문 (Questions)

### Question 1
Backend API 구조는 어떤 패턴을 선호하십니까?

A) Layered Architecture (Controller → Service → Repository) - 전통적인 Spring Boot 패턴
B) Hexagonal Architecture (Port & Adapter) - 도메인 중심 설계
C) Other (please describe after [Answer]: tag below)

[Answer]: A

### Question 2
Frontend 상태 관리는 어떤 방식을 선호하십니까?

A) React Context API + useReducer (경량, 추가 라이브러리 없음)
B) Zustand (경량 상태 관리 라이브러리)
C) Redux Toolkit (대규모 상태 관리)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

### Question 3
Frontend 라우팅 구조는 어떻게 하시겠습니까? (고객용과 관리자용이 별도 앱인지)

A) 단일 React 앱 내에서 라우팅으로 분리 (/customer/*, /admin/*)
B) 고객용/관리자용 별도 React 앱 (2개 프로젝트)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## 실행 계획 (Execution Plan)

- [x] 컴포넌트 식별 및 책임 정의 (components.md)
- [x] 컴포넌트 메서드 시그니처 정의 (component-methods.md)
- [x] 서비스 레이어 설계 (services.md)
- [x] 컴포넌트 의존성 관계 정의 (component-dependency.md)
- [x] 설계 완전성 및 일관성 검증
