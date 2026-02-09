# Database - NFR Design 패턴

## 성능 패턴

### 인덱스 전략
- 복합 인덱스: 주요 조회 패턴에 맞춰 설계 (store_id 기반)
- 커버링 인덱스: 자주 조회되는 컬럼 포함

### 커넥션 풀
- HikariCP: max 10, min-idle 5, connection-timeout 30s

## 데이터 무결성 패턴

### 트랜잭션 관리
- 이용 완료(completeTable): @Transactional로 Order→OrderHistory 이동 원자성 보장
- 주문 생성: @Transactional로 Order + OrderItem + 세션 업데이트 원자성 보장

### 스냅샷 패턴
- OrderItem에 menu_name, unit_price 스냅샷 저장 (메뉴 변경 시 기존 주문 영향 없음)
