# TDD Code Generation Plan - backend

## Unit Context
- **Workspace Root**: /Users/kirby/Desktop/project/ai-dlc/avengers
- **Project Type**: Greenfield (multi-unit)
- **Code Location**: backend/
- **Stories**: US-C01~C05, US-C13~C16, US-A01~A16 (28개)

## Plan Step 0: 프로젝트 구조 + Contract Skeleton 생성
- [x] Spring Boot 프로젝트 구조 생성 (build.gradle, application.yml)
- [x] Entity 클래스 skeleton (9개)
- [x] DTO 클래스 skeleton (Request/Response)
- [x] Repository 인터페이스 skeleton (9개)
- [x] Service 클래스 skeleton (5개, NotImplementedError)
- [x] Controller 클래스 skeleton (5개)
- [x] Security 클래스 skeleton (SecurityConfig, JwtUtil, JwtFilter)
- [x] Exception 클래스 skeleton (GlobalExceptionHandler)
- [x] 컴파일 확인

## Plan Step 1: Security Layer
- [x] JwtUtil - RED-GREEN-REFACTOR

## Plan Step 2: AuthService (TDD)
- [x] AuthService.tableLogin() - RED-GREEN-REFACTOR (TC-BE-001, TC-BE-002)
- [x] AuthService.adminLogin() - RED-GREEN-REFACTOR (TC-BE-003~006)

## Plan Step 3: MenuService (TDD)
- [x] MenuService - 전체 구현 완료 (TC-BE-007~013)

## Plan Step 4: OrderService (TDD)
- [x] OrderService - 전체 구현 완료 (TC-BE-014~022)

## Plan Step 5: TableService (TDD)
- [x] TableService - 전체 구현 완료 (TC-BE-023~027)

## Plan Step 6: SSEService (TDD)
- [x] SSEService - 전체 구현 완료 (TC-BE-028~029)

## Plan Step 7: Controller Layer + Security Config
- [x] 전체 Controller + Security 구현 완료

## Plan Step 8: 통합 검증
- [ ] 전체 테스트 실행
- [ ] 컴파일 확인
