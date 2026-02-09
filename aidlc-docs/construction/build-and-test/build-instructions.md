# Build Instructions

## Prerequisites
- **Java**: 21 (Spring Boot 3.4.2)
- **Node.js**: 16+ (Vite 5.2.14, React 18)
- **PostgreSQL**: 16
- **Gradle**: Wrapper 포함 (별도 설치 불필요)

## 1. Database Setup

```bash
# PostgreSQL 서비스 시작
brew services start postgresql@16  # macOS
# 또는: sudo systemctl start postgresql  # Linux

# 데이터베이스 생성
createdb tableorder

# 마이그레이션 실행 (Flyway가 Spring Boot 시작 시 자동 실행)
# 수동 실행 시:
psql -d tableorder -f database/migrations/V1__create_tables.sql
psql -d tableorder -f database/seed/V99__seed_data.sql
```

## 2. Backend Build

```bash
cd backend

# 의존성 설치 + 빌드
./gradlew clean build

# 빌드만 (테스트 제외)
./gradlew clean build -x test
```

- **빌드 결과물**: `backend/build/libs/table-order-0.0.1-SNAPSHOT.jar`
- **성공 기준**: `BUILD SUCCESSFUL` 메시지

## 3. Frontend Build

```bash
cd frontend

# 의존성 설치
npm install

# 타입 체크
npx tsc --noEmit

# 프로덕션 빌드
npx vite build
```

- **빌드 결과물**: `frontend/dist/` 디렉토리
- **성공 기준**: `✓ built in` 메시지

## 4. 전체 서비스 실행

```bash
# 1) Backend 실행 (포트 8080)
cd backend
./gradlew bootRun

# 2) Frontend 개발 서버 (포트 5173, API 프록시 → 8080)
cd frontend
npm run dev
```

- **접속**: http://localhost:5173

## Troubleshooting

### PostgreSQL 연결 실패
- `application.yml`에서 DB URL, username, password 확인
- PostgreSQL 서비스 실행 상태 확인: `pg_isready`

### Gradle 빌드 실패
- Java 21 설치 확인: `java -version`
- JAVA_HOME 환경변수 확인

### Frontend 빌드 실패
- Node.js 16+ 확인: `node -v`
- `node_modules` 삭제 후 재설치: `rm -rf node_modules && npm install`
