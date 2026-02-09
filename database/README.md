# Database - 테이블오더 서비스

## 구조
```
database/
  migrations/
    V1__create_tables.sql    # 스키마 DDL
  seed/
    V99__seed_data.sql       # 테스트 시드 데이터
  tests/
    validate_schema.sql      # 스키마 검증 쿼리
```

## 실행 방법
Flyway를 통해 Spring Boot 시작 시 자동 실행되거나, 수동으로 PostgreSQL에 적용:

```bash
psql -U postgres -d tableorder -f migrations/V1__create_tables.sql
psql -U postgres -d tableorder -f seed/V99__seed_data.sql
```
