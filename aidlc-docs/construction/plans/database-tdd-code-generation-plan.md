# TDD Code Generation Plan - database

## Unit Context
- **Workspace Root**: /Users/kirby/Desktop/project/ai-dlc/avengers
- **Project Type**: Greenfield (multi-unit)
- **Code Location**: database/
- **Stories**: US-C01, US-C03, US-C13, US-A01, US-A08, US-A10

## Plan Step 0: 프로젝트 구조 생성
- [x] database/ 디렉토리 생성
- [x] database/migrations/ 디렉토리 생성
- [x] database/seed/ 디렉토리 생성
- [x] database/README.md 생성

## Plan Step 1: 스키마 마이그레이션 (TDD)
- [x] V1__create_tables.sql - RED-GREEN-REFACTOR
  - [x] RED: TC-DB-001~007 테스트 작성 (스키마 검증 SQL)
  - [x] GREEN: DDL 작성 (9개 테이블, 인덱스, 제약조건)
  - [x] REFACTOR: DDL 정리 및 검증
  - [x] VERIFY: 모든 테스트 통과

## Plan Step 2: 시드 데이터 (TDD)
- [x] V99__seed_data.sql - RED-GREEN-REFACTOR
  - [x] RED: TC-DB-008 테스트 작성 (시드 데이터 검증)
  - [x] GREEN: INSERT 문 작성
  - [x] REFACTOR: 데이터 정리
  - [x] VERIFY: 테스트 통과
