# Backend - 비즈니스 로직 모델

## AuthService 로직

### tableLogin(storeCode, tableNo, password)
```
1. Store 조회 by storeCode → 없으면 401
2. TableInfo 조회 by (store_id, table_no) → 없으면 401
3. bcrypt.matches(password, table.password) → 불일치 시 401
4. JWT 발급 (payload: storeId, tableId, tableNo, role=TABLE, exp=16h)
5. return TokenResponse(token, storeId, tableId, tableNo)
```

### adminLogin(storeCode, username, password)
```
1. Store 조회 by storeCode → 없으면 401
2. Admin 조회 by (store_id, username) → 없으면 401
3. 잠금 확인: admin.locked_until > now() → 423 Locked
4. bcrypt.matches(password, admin.password) → 불일치 시:
   a. admin.failed_attempts += 1
   b. failed_attempts >= 10 → admin.locked_until = now() + 30분
   c. return 401
5. 성공 시: admin.failed_attempts = 0, admin.locked_until = null
6. JWT 발급 (payload: storeId, adminId, role=ADMIN, exp=16h)
7. return TokenResponse(token, storeId)
```

## MenuService 로직

### createMenu(storeId, request)
```
1. 검증: name NOT BLANK, price >= 0, category 존재 확인
2. sort_order: 해당 카테고리 내 MAX(sort_order) + 1
3. Menu 엔티티 생성 및 저장
4. return MenuResponse
```

### updateMenuOrder(storeId, request)
```
1. request: [{menuId, sortOrder}, ...]
2. 각 메뉴의 sort_order 일괄 업데이트
```

## OrderService 로직

### createOrder(storeId, tableId, request)
```
1. 검증: items 비어있지 않음, 각 item의 menuId 유효, quantity > 0
2. 세션 확인:
   a. TableInfo 조회
   b. session_id가 null이면 (첫 주문):
      - 새 session_id 생성: "{tableNo}-{yyyyMMddHHmmss}"
      - table.session_id = 새 session_id
      - table.status = 'OCCUPIED'
   c. session_id가 있으면 기존 세션 사용
3. Order 생성 (id=UUID, status=PENDING)
4. OrderItem 생성 (menu_name, unit_price는 현재 메뉴에서 스냅샷)
5. total_amount 계산 = SUM(quantity * unit_price)
6. 저장
7. SSEService.broadcastOrderEvent(storeId, NEW_ORDER, orderData)
8. return OrderResponse(orderId, orderNo, ...)
```

### updateOrderStatus(orderId, newStatus)
```
1. Order 조회 → 없으면 404
2. 상태 전이 검증 (순방향만):
   - PENDING → PREPARING (허용)
   - PREPARING → COMPLETED (허용)
   - 그 외 → 400 Bad Request
3. order.status = newStatus
4. 저장
5. SSEService.broadcastOrderEvent(storeId, STATUS_CHANGED, orderData)
6. return OrderResponse
```

### deleteOrder(orderId)
```
1. Order 조회 → 없으면 404
2. Order + OrderItem 삭제 (CASCADE)
3. SSEService.broadcastOrderEvent(storeId, ORDER_DELETED, {orderId, tableId})
```

## TableService 로직

### completeTable(storeId, tableId)
```
1. TableInfo 조회 → 없으면 404
2. session_id가 null이면 → 400 (이미 빈 테이블)
3. 현재 세션의 모든 Order 조회
4. 각 Order → OrderHistory로 복사 (completed_at = now())
5. 각 OrderItem → OrderHistoryItem으로 복사
6. 원본 Order + OrderItem 삭제
7. table.session_id = null, table.status = 'EMPTY'
8. 저장
9. SSEService.broadcastOrderEvent(storeId, TABLE_COMPLETED, {tableId})
```

### getTableHistory(storeId, tableId, dateFilter)
```
1. OrderHistory 조회 by (store_id, table_id)
2. dateFilter가 있으면: completed_at의 날짜 필터 적용
3. 시간 역순 정렬
4. return List<OrderHistoryResponse>
```

## SSEService 로직

### subscribeAdmin(storeId)
```
1. SseEmitter 생성 (timeout=16h)
2. adminEmitters[storeId]에 추가
3. onCompletion/onTimeout/onError 시 제거
4. return SseEmitter
```

### subscribeTable(storeId, tableId)
```
1. SseEmitter 생성 (timeout=16h)
2. tableEmitters[storeId:tableId]에 추가
3. onCompletion/onTimeout/onError 시 제거
4. return SseEmitter
```

### broadcastOrderEvent(storeId, eventType, data)
```
1. adminEmitters[storeId]의 모든 emitter에 전송
2. data에 tableId가 있으면 tableEmitters[storeId:tableId]에도 전송
3. 전송 실패한 emitter는 제거
```
