# Backend - 기술 스택 결정

## 핵심 스택
| 항목 | 결정 | 버전 |
|------|------|------|
| Language | Java | 21 (LTS) |
| Framework | Spring Boot | 3.4.x |
| Build Tool | Gradle | Groovy DSL |
| ORM | Spring Data JPA + Hibernate | Spring Boot 관리 |
| Database | PostgreSQL | 16 |
| Migration | Flyway | Spring Boot 관리 |

## 주요 의존성
| 라이브러리 | 용도 |
|-----------|------|
| spring-boot-starter-web | REST API |
| spring-boot-starter-data-jpa | JPA/Hibernate |
| spring-boot-starter-security | 인증/인가 |
| spring-boot-starter-validation | Bean Validation |
| jjwt (io.jsonwebtoken) | JWT 생성/검증 |
| postgresql | PostgreSQL JDBC 드라이버 |
| lombok | 보일러플레이트 코드 감소 |
| springdoc-openapi | Swagger/OpenAPI 문서 |
| spring-boot-starter-test | 테스트 |
