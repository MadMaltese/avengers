package com.tableorder.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SSEService {
    private final Map<Long, List<SseEmitter>> adminEmitters = new ConcurrentHashMap<>();
    private final Map<String, List<SseEmitter>> tableEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribeAdmin(Long storeId) {
        var emitter = new SseEmitter(57600000L); // 16h
        adminEmitters.computeIfAbsent(storeId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeAdmin(storeId, emitter));
        emitter.onTimeout(() -> removeAdmin(storeId, emitter));
        emitter.onError(e -> removeAdmin(storeId, emitter));
        return emitter;
    }

    public SseEmitter subscribeTable(Long storeId, Long tableId) {
        String key = storeId + ":" + tableId;
        var emitter = new SseEmitter(57600000L);
        tableEmitters.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeTable(key, emitter));
        emitter.onTimeout(() -> removeTable(key, emitter));
        emitter.onError(e -> removeTable(key, emitter));
        return emitter;
    }

    public void broadcastOrderEvent(Long storeId, String eventType, Object data) {
        sendToEmitters(adminEmitters.get(storeId), eventType, data);
    }

    public void broadcastToTable(Long storeId, Long tableId, String eventType, Object data) {
        sendToEmitters(tableEmitters.get(storeId + ":" + tableId), eventType, data);
    }

    private void sendToEmitters(List<SseEmitter> emitters, String eventType, Object data) {
        if (emitters == null) return;
        var dead = new ArrayList<SseEmitter>();
        for (var emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name(eventType).data(data));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }

    private void removeAdmin(Long storeId, SseEmitter emitter) {
        var list = adminEmitters.get(storeId);
        if (list != null) list.remove(emitter);
    }

    private void removeTable(String key, SseEmitter emitter) {
        var list = tableEmitters.get(key);
        if (list != null) list.remove(emitter);
    }
}
