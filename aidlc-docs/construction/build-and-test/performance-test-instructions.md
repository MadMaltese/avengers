# Performance Test Instructions

## 성능 요구사항
- **API 응답 시간**: p95 < 200ms
- **동시 사용자**: 20 테이블 (소규모 단일 매장)
- **SSE 연결**: 최대 21개 (20 테이블 + 1 관리자)

## 테스트 도구
Apache Bench (`ab`) 또는 `wrk` 사용 (간단한 부하 테스트)

## 테스트 실행

### 1. 메뉴 조회 API 부하 테스트
```bash
# 100 요청, 동시 20 연결
ab -n 100 -c 20 -H "Authorization: Bearer {TOKEN}" \
  http://localhost:8080/api/stores/1/menus
```
- **기대**: p95 < 200ms, 에러율 0%

### 2. 주문 생성 API 부하 테스트
```bash
# POST 요청 파일 준비
echo '{"items":[{"menuId":1,"quantity":1}]}' > /tmp/order.json

ab -n 50 -c 10 -p /tmp/order.json -T "application/json" \
  -H "Authorization: Bearer {TOKEN}" \
  http://localhost:8080/api/stores/1/tables/1/orders
```
- **기대**: p95 < 200ms

### 3. SSE 동시 연결 테스트
```bash
# 20개 동시 SSE 연결
for i in $(seq 1 20); do
  curl -sN "http://localhost:8080/api/stores/1/tables/$i/sse/orders?token={TOKEN}" > /dev/null &
done
# 관리자 SSE
curl -sN "http://localhost:8080/api/stores/1/sse/orders?token={ADMIN_TOKEN}" > /dev/null &

# 연결 상태 확인
jobs | wc -l
# 기대: 21개 연결 유지
```

## 성능 기준
| 항목 | 목표 | 비고 |
|------|------|------|
| 메뉴 조회 p95 | < 200ms | GET /api/stores/{id}/menus |
| 주문 생성 p95 | < 200ms | POST /api/.../orders |
| SSE 동시 연결 | 21개 | 20 테이블 + 1 관리자 |
| 에러율 | 0% | 정상 요청 기준 |

## 참고
- 소규모 단일 매장 (≤20 테이블) 대상이므로 대규모 부하 테스트는 불필요
- 로컬 환경에서 위 기준 충족 시 운영 환경에서도 충분
