# 테이블오더 서비스 - 스토리-유닛 매핑

## 매핑 테이블

| 스토리 | 설명 | database | backend | frontend |
|--------|------|----------|---------|----------|
| US-C01 | 테이블 태블릿 초기 설정 | ✅ TableInfo | ✅ AuthService | ✅ SetupPage |
| US-C02 | 자동 로그인 | - | ✅ AuthService | ✅ AuthModule |
| US-C03 | 카테고리별 메뉴 목록 조회 | ✅ Category, Menu | ✅ MenuService | ✅ MenuPage |
| US-C04 | 카테고리 간 이동 | - | ✅ MenuService | ✅ MenuPage |
| US-C05 | 메뉴 상세 정보 조회 | - | ✅ MenuService | ✅ MenuPage |
| US-C06 | 장바구니에 메뉴 추가 | - | - | ✅ CartModule |
| US-C07 | 장바구니 수량 조절 | - | - | ✅ CartModule |
| US-C08 | 장바구니 메뉴 삭제 | - | - | ✅ CartModule |
| US-C09 | 장바구니 총 금액 표시 | - | - | ✅ CartModule |
| US-C10 | 장바구니 비우기 | - | - | ✅ CartModule |
| US-C11 | 장바구니 로컬 저장 | - | - | ✅ CartModule |
| US-C12 | 주문 내역 최종 확인 | - | - | ✅ OrderModule |
| US-C13 | 주문 확정 | ✅ Order, OrderItem | ✅ OrderService | ✅ OrderModule |
| US-C14 | 주문 실패 처리 | - | ✅ OrderService | ✅ OrderModule |
| US-C15 | 현재 세션 주문 목록 조회 | - | ✅ OrderService | ✅ OrderModule |
| US-C16 | 주문 상태 실시간 업데이트 | - | ✅ SSEService | ✅ SSE Hook |
| US-A01 | 관리자 로그인 | ✅ Admin | ✅ AuthService | ✅ LoginPage |
| US-A02 | 세션 유지 및 자동 로그아웃 | - | ✅ SecurityConfig | ✅ AuthModule |
| US-A03 | 테이블별 주문 대시보드 조회 | - | ✅ OrderService | ✅ DashboardPage |
| US-A04 | 실시간 신규 주문 수신 | - | ✅ SSEService | ✅ SSE Hook |
| US-A05 | 주문 상세 보기 | - | ✅ OrderService | ✅ DashboardPage |
| US-A06 | 주문 상태 변경 | - | ✅ OrderService | ✅ DashboardPage |
| US-A07 | 테이블별 필터링 | - | ✅ OrderService | ✅ DashboardPage |
| US-A08 | 테이블 초기 설정 | ✅ TableInfo | ✅ TableService | ✅ TableMgmt |
| US-A09 | 주문 삭제 | - | ✅ OrderService | ✅ DashboardPage |
| US-A10 | 테이블 이용 완료 처리 | ✅ OrderHistory | ✅ TableService | ✅ DashboardPage |
| US-A11 | 과거 주문 내역 조회 | - | ✅ TableService | ✅ DashboardPage |
| US-A12 | 메뉴 목록 조회 | - | ✅ MenuService | ✅ MenuMgmtPage |
| US-A13 | 메뉴 등록 | - | ✅ MenuService | ✅ MenuMgmtPage |
| US-A14 | 메뉴 수정 | - | ✅ MenuService | ✅ MenuMgmtPage |
| US-A15 | 메뉴 삭제 | - | ✅ MenuService | ✅ MenuMgmtPage |
| US-A16 | 메뉴 노출 순서 조정 | - | ✅ MenuService | ✅ MenuMgmtPage |

## 유닛별 스토리 수

| 유닛 | 스토리 수 | 비고 |
|------|-----------|------|
| **database** | 7개 | 스키마 관련 스토리만 |
| **backend** | 28개 | 장바구니(C06~C11) 제외 전체 |
| **frontend** | 32개 | 전체 스토리 |

## 유닛별 스토리 목록

### database
US-C01, US-C03, US-C13, US-A01, US-A08, US-A10 + 전체 테이블 스키마

### backend
US-C01~C05, US-C13~C16, US-A01~A16 (28개)

### frontend
US-C01~C16, US-A01~A16 (32개 전체)
