package com.tableorder.controller;

import com.tableorder.dto.request.TableSetupRequest;
import com.tableorder.dto.response.*;
import com.tableorder.service.TableService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/stores/{storeId}/tables") @RequiredArgsConstructor
public class TableController {
    private final TableService tableService;

    @PostMapping
    public ResponseEntity<TableResponse> setupTable(@PathVariable Long storeId, @Valid @RequestBody TableSetupRequest req) {
        return ResponseEntity.status(201).body(tableService.setupTable(storeId, req));
    }

    @GetMapping
    public List<TableResponse> getTables(@PathVariable Long storeId) {
        return tableService.getTablesByStore(storeId);
    }

    @PostMapping("/{tableId}/complete")
    public ResponseEntity<Void> completeTable(@PathVariable Long storeId, @PathVariable Long tableId) {
        tableService.completeTable(storeId, tableId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tableId}/history")
    public List<OrderHistoryResponse> getHistory(@PathVariable Long storeId, @PathVariable Long tableId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return tableService.getTableHistory(storeId, tableId, date);
    }
}
