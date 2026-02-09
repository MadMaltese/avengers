package com.tableorder.controller;

import com.tableorder.service.SSEService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController @RequiredArgsConstructor
public class SSEController {
    private final SSEService sseService;

    @GetMapping(value = "/api/stores/{storeId}/sse/orders", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeAdmin(@PathVariable Long storeId) {
        return sseService.subscribeAdmin(storeId);
    }

    @GetMapping(value = "/api/stores/{storeId}/tables/{tableId}/sse/orders", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeTable(@PathVariable Long storeId, @PathVariable Long tableId) {
        return sseService.subscribeTable(storeId, tableId);
    }
}
